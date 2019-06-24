package com.kssj.frame.mongo;


import org.springframework.beans.factory.config.AbstractFactoryBean;
import com.mongodb.Mongo;
import com.mongodb.MongoOptions;
import com.mongodb.ReadPreference;
import com.mongodb.ServerAddress;
import com.mongodb.WriteConcern;

@SuppressWarnings("rawtypes")
public class MongoFactoryBean extends AbstractFactoryBean {

	// 表示服务器列表(主从复制或者分片)的字符串数组
	private String serverString;
	// mongoDB配置对象
	private MongoOptions mongoOptions;
	// 是否主从分离(读取从库)，默认读写都在主库
	private boolean readSecondary = false;
	// 设定写策略(出错时是否抛异常)，默认采用SAFE模式(需要抛异常)
	private WriteConcern writeConcern = WriteConcern.SAFE;

	@Override
	public Class<?> getObjectType() {
		return Mongo.class;
	}

	@Override
	protected Mongo createInstance() throws Exception {
		Mongo mongo = initMongo();

		// 设定主从分离
		if (readSecondary) {
			mongo.setReadPreference(ReadPreference.secondaryPreferred());
		}

		// 设定写策略
		mongo.setWriteConcern(writeConcern);
		return mongo;
	}

	/**
	 * 初始化mongo实例
	 * @return
	 * @throws Exception
	 */
	private Mongo initMongo() throws Exception {
		// 根据条件创建Mongo实例
		Mongo mongo = null;
		ServerAddress serverAddress  = getServerList();

		if (serverAddress == null) {
			mongo = new Mongo();
		}else{
			if (mongoOptions != null) {
				mongo = new Mongo(serverAddress, mongoOptions);
			}else{
				mongo = new Mongo(serverAddress);
			}
		}
		return mongo;
	}


	/**
	 * 根据服务器字符串列表，解析出服务器对象列表
	 * <p>
	 * 
	 * @Title: getServerList
	 *         </p>
	 * 
	 * @return
	 * @throws Exception
	 */
	private ServerAddress getServerList() throws Exception {
		ServerAddress serverAddress = null;
		try {
				String[] temp = serverString.split(":");
				String host = temp[0];
				if (temp.length > 2) {
					throw new IllegalArgumentException(
							"Invalid server address string: " + serverString);
				}
				if (temp.length == 2) {
					serverAddress = new ServerAddress(host, Integer.parseInt(temp[1]));
				} else {
					serverAddress = new ServerAddress(host);
				}
			return serverAddress;
		} catch (Exception e) {
			throw new Exception(
					"Error while converting serverString to ServerAddressList",
					e);
		}
	}

	public MongoOptions getMongoOptions() {
		return mongoOptions;
	}

	public void setMongoOptions(MongoOptions mongoOptions) {
		this.mongoOptions = mongoOptions;
	}

	public String getServerString() {
		return serverString;
	}

	public void setServerString(String serverString) {
		this.serverString = serverString;
	}

	public boolean isReadSecondary() {
		return readSecondary;
	}

	public void setReadSecondary(boolean readSecondary) {
		this.readSecondary = readSecondary;
	}

	public WriteConcern getWriteConcern() {
		return writeConcern;
	}

	public void setWriteConcern(WriteConcern writeConcern) {
		this.writeConcern = writeConcern;
	}

	
	
	

	/* ------------------- setters --------------------- */
}