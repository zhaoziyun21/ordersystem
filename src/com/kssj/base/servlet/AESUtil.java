package com.kssj.base.servlet;


import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.AlgorithmParameterSpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

  
/** 
 * 通过AES算法对文本进行加密解密 
 * @author ShaoJiang 
 * 
 */  
public class AESUtil {  
    private static SecretKey key;                       //加密密钥  
    private static AlgorithmParameterSpec paramSpec;    //算法参数  
    private static Cipher ecipher;                      //加密算法  
    static String charset="UTF-8";
    
    
    public static void main(String[] args) throws Exception {
		// 需要加密的字串
		String cSrc = "<ABCB2I><Header><TransDate>20140621</TransDate><TransTime>122504</TransTime><TransCode>1002</TransCode><!--交易编码--><SerialNo>0520140621010008</SerialNo><!--银行交易流水号--><CorpNo>4518</CorpNo><!--保险公司代码--><BankCode>05</BankCode><ProvCode>3601</ProvCode><!--省市代码--><BranchNo>360101019</BranchNo><!--网点号--><TransSide>1</TransSide><!--交易发起方0:保险公司；1:银行--><Tlid>dsfds</Tlid><!--柜员号--><EntrustWay></EntrustWay><!--委托方式--></Header><App><Req><AppNo>99001256321450</AppNo><ProvCode>11</ProvCode><BranchNo>0102</BranchNo><Base><SpecArranged>0</SpecArranged><!--特别约定--><ArugeFlag>0</ArugeFlag><!--争议处理方式--><ArbteName>AAAAA</ArbteName><!--仲裁机构名称--><ConAccName>小明</ConAccName><!--续期缴费账户名称--><ConAccNo>6226789654123</ConAccNo><!--续期交费账(卡)号--><ApplGetAccNo>6226789654123</ApplGetAccNo><!--投保人领取账(卡)号--><InsuGetAccNo>6226789654123</InsuGetAccNo><!--被保人领取账(卡)号--><ApplyDate>20140618</ApplyDate><!--投保日期--><PolicyApplySerial>31100600000001</PolicyApplySerial><!--投保单号--><VchType></VchType><!--凭证种类--><VchNo>32500500000002</VchNo><!--印刷号码--><Saler>ABC001</Saler><!--银行销售人员编号--><SalerCertNo></SalerCertNo><!--人员资格证编号--><BranchCertNo></BranchCertNo><!--网点兼业代理许可证编号--><BranchName></BranchName><!--网点名称--></Base><Appl><IDKind>110001</IDKind><IDCode>150105195407059016</IDCode><BeginDate>20100101</BeginDate><InvalidDate>20191019</InvalidDate><Name>小明</Name><Sex>0</Sex><Birthday>19540705</Birthday><Country>156</Country><Prov>北京</Prov><City></City><Zone>海淀</Zone><Address>北京海淀区东南</Address><ZipCode>100086</ZipCode><Email>abchina@sina.com</Email><Phone>01087517898</Phone><Mobile>15110987876</Mobile><OtherConnect></OtherConnect><!--投保人其他电子联系方式--><ShortMsg></ShortMsg><!--是否接收手机短信服务--><FixIncome></FixIncome><!--是否有固定收入--><AnnualIncome>20</AnnualIncome><!--投保人年收入--><Company>中国国防部日本情报科</Company><!--工作单位--><JobType></JobType><!--职业类别--><JobCode>0001001</JobCode><!--职业代码--><CustSource></CustSource><!--客户来源--><Notice></Notice><!--投保人告知--><RelaToInsured>01</RelaToInsured><!--与被保人关系--></Appl><Insu><IDKind>110001</IDKind><IDCode>150105195407059016</IDCode><BeginDate>20100101</BeginDate><InvalidDate>20191019</InvalidDate><Name>小明</Name><Sex>0</Sex><Birthday>19540705</Birthday><Country>156</Country><Prov>北京</Prov><City></City><Zone>海淀</Zone><Address>中国国防部日本情报科</Address><ZipCode>100086</ZipCode><Email>abchina@sina.com</Email><Phone>01087517898</Phone><Mobile>15110987876</Mobile><JobType></JobType><JobCode>0001001</JobCode><Company></Company><AnnualIncome>20</AnnualIncome><Tall>174</Tall><!--被保人身高--><Weight>70</Weight><!--被保人体重--><IsRiskJob>0</IsRiskJob><!--是否危险职业--><HealthNotice>0</HealthNotice><!--被保人健康告知--><Notice></Notice><!--被保人告知内容--><CreditCard></CreditCard><!--信用卡卡号--><CreditType></CreditType><!--信用卡类别--><Immaturity></Immaturity><!--未成年人其他风险保额--><RelaToInsured>01</RelaToInsured><!----></Insu><Bnfs><Count>1</Count><Type1>1</Type1><Name1>小红</Name1><Sex1>0</Sex1><Birthday1>20120506</Birthday1><IDKind1>110005</IDKind1><IDCode1>45236987</IDCode1><BeginDate1>20120507</BeginDate1><InvalidDate1>20201015</InvalidDate1><RelationToInsured1>07</RelationToInsured1><Sequence1>1</Sequence1><Prop1>100</Prop1><!--受益人1受益比例--><Phone1>45236541</Phone1><Country1></Country1><!--国籍--><GetAccNo1></GetAccNo1><!--受益人领取账(卡)号--><Address1></Address1><!--受益人通讯地址--><Prov1></Prov1><!--省(直辖市)--><City1></City1><!--市--><Zone1></Zone1><!--区--></Bnfs><Risks><Count>1</Count><!--险种个数--><RiskCode>312204</RiskCode><Name>财富人生两全保险</Name><PassWord>222222</PassWord><!--保单密码--><Share>10</Share><!--主险份数--><Prem>10000.00</Prem><!--主险保险费--><PayType>1</PayType><!--交费方式--><PayDueType>3</PayDueType><!--交费期间类型--><PayDueDate>10</PayDueDate><!--交费期间--><InsuDueType>4</InsuDueType><!--保险期间类型--><InsuDueDate>5</InsuDueDate><!--保险期间--><FullBonusGetMode></FullBonusGetMode><!--满期保险金领取方式--><GetYearFlag></GetYearFlag><!--年金/生存金领取方式--><GetYear></GetYear><!--领取年期/年龄--><BonusGetMode></BonusGetMode><!--红利领取方式--><BonusPayMode></BonusPayMode><!--红利分配方式--><WNRiskAccCount></WNRiskAccCount><!--万能险账户个数--><WNRiskAccType></WNRiskAccType><!--万能险账户类型--><InvestProtion></InvestProtion><!--投资比例--><AutoPayFlag></AutoPayFlag><!--自动续保标志--><AutoPayForFlag></AutoPayForFlag><!--自动垫交标记--><SubFlag></SubFlag><!--减额交清标记--><RiskBeginDate></RiskBeginDate><!--保险起期--><RiskEndDate></RiskEndDate><!--保险止期--><InvestType></InvestType><!--投资方式--></Risks></Req></App></ABCB2I> ";
		
		String ySrc="6F93EB2D3D2813BF04D8E849A7668D80962981440979298474B57395B7B0F897E6BD664A9FE7F77A494A143121909E56A98BC7F250EEA7C60C250ED27ADC58BF526CD2861F8B7011D5BC5363DB2FD5C7F3D9682B8064EA64217E8DEE30445ED912692695F214E0D5A0FB732FF8DE12A59E5DDF23C545A60DEE102DB7421744033C78963AD887AA239B79F19E0402BAD807B6613DDA1964704179DD222413895111C1B8445DCCE1B6493707297F06208D9575B90A87ED19563B95133EDF03173527148FEFCD766F4CCCAF0CADEEDE14EDA6809027F4278F41F0940B914F8BE1150AC9D01B5AF5C9FD55629603E56BA6E30BE231A3DC6906CEC5A22B1E598261BDFB424FD2949F05499845A8B61AB4ABD95BEA9C9BC6CAAAD7980BB79FDF6E30356CB17308B4DE0BEA2A0D442882BD6E576942957A2AEFAFBE666B489FC2AE21581EB345C83C8B272948755CFB9186F052628A753170E3E88E282A004D0CC170C243D41C438545739E2FE81D9FB17D1ADA32A9B27998CD134FD8F2693510AECA0188DD616E451669579479C85172950609E0D58727AF5A49C71DA83A86338C4FC1520130619694D17A1C9C3D318E3A31D410DDCA89D169B1332187E0BA4FD10F8ABE6D7347A0BBFBA0082918F90E9C7446109194AE75FAA9B4AADEBA2BEE52FDEC57B8446D24AEF428663E84A5C14963413914E04A550EFB6A695CBD6A7FB81605B55247B675E30753ED37BE340C6AF9C69DCDE5A757DC1F680989200595D8BDDD2CBF92437DFCCF825E41DC5CA67A2B2A6BD406405B9C2A2B1BE6FB103EE06E9EF16E1DA884D0D4F50708AF4C2C168DA674EA340BE932FEDADF0A9392B7A96BD03F6AA030614473684B26F3F9FB74551967B6EB073F958538D41F2EA9BEEB5972A3045A6C136C6FF66399D1CAFC9A45A714DCC4BA1FAD9038F4F20B0B31647DDC87897E0387DF927FC55C35AC28221E79F8589BBEBFA5EBB2CB234A639838554AF04130244679C770888C2F128D7BA83DFBA6832628D06235D904BEDD03F941CE";
		cSrc="aaaaaaaaaaaa";
		// 加密
		long lStart = System.currentTimeMillis();
		String enString="";
		
		enString = encrypt(cSrc).toUpperCase();
		System.out.println("加密后的字串是：" + enString);
		
		System.out.println("正确的的字串是：" + ySrc);
		long lUseTime = System.currentTimeMillis() - lStart;
		System.out.println("加密耗时：" + lUseTime + "毫秒");
		// 解密
		lStart = System.currentTimeMillis();
		String DeString = decrypt(enString);
		System.out.println("解密后的字串是：" + DeString);
		lUseTime = System.currentTimeMillis() - lStart;
		System.out.println("解密耗时：" + lUseTime + "毫秒");
		
	}
      
    static{       
        try {  
        	String t_key=Global.getConfig("AES_KEY");
        	String t_iv=Global.getConfig("AES_IV");
        	t_key=(t_key==null || "".equals(t_key)) ? "3yrQpIxKCsC49jGi": t_key;
        	t_iv=(t_iv==null || "".equals(t_iv)) ? "ABCHINA..ANIHCBA": t_iv;
        	byte[] keyValue =t_key.getBytes();       //用户密钥  
            byte[] iv =t_iv.getBytes();            //算法参数  
            String mode="AES/CBC/NoPadding";//加解密模式
            
        	key = new SecretKeySpec(keyValue, "AES");
            //使用iv中的字节作为IV来构造一个 算法参数。  
            paramSpec = new IvParameterSpec(iv);  
            //生成一个实现指定转换的 Cipher 对象  
            ecipher = Cipher.getInstance(mode);         
        } catch (NoSuchAlgorithmException e) {  
            e.printStackTrace();  
        } catch (NoSuchPaddingException e) {  
            e.printStackTrace();  
        }  
    }  
  

    /** 
     * 加密，使用指定数据源生成密钥，使用用户数据作为算法参数进行AES加密 
     * @param msg 加密的数据 
     * @return 
     */  
    public static String encrypt(String msg) {  
        String str = "";  
        try {             
            //用密钥和一组算法参数初始化此 cipher  
            ecipher.init(Cipher.ENCRYPT_MODE, key, paramSpec);  
            System.out.println("encrypt con :"+msg);
            System.out.println("encrypt length:"+msg.getBytes(charset).length);
            int mod=16-msg.getBytes(charset).length%16;
            System.out.println("encrypt con mod is:"+mod);
            if(mod!=0){
            	msg=RCh(msg," ",mod);
            	System.out.println("transfer after encrypt con :"+msg);
            	System.out.println("transfer after encrypt length:"+msg.getBytes(charset).length);
            }
            
            //加密并转换成16进制字符串  
			str = asHex(ecipher.doFinal(msg.getBytes(charset)));
        }catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}catch (BadPaddingException e) {  
            e.printStackTrace();  
        } catch (InvalidKeyException e) {  
            e.printStackTrace();  
        } catch (InvalidAlgorithmParameterException e) {  
            e.printStackTrace();  
        } catch (IllegalBlockSizeException e) {  
            e.printStackTrace();  
        }  
        return str;  
    }  
      
    /** 
     * 解密，对生成的16进制的字符串进行解密 
     * @param value 解密的数据 
     * @return 
     */  
    public static String decrypt(String value) {  
        try {  
            ecipher.init(Cipher.DECRYPT_MODE, key, paramSpec);  
			return new String(ecipher.doFinal(asBin(value)),"UTF-8").trim();
        } catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  catch (BadPaddingException e) {  
            e.printStackTrace();  
        } catch (InvalidKeyException e) {  
            e.printStackTrace();  
        } catch (InvalidAlgorithmParameterException e) {  
            e.printStackTrace();  
        } catch (IllegalBlockSizeException e) {  
            e.printStackTrace();  
        }  
        return "";  
    }  
  
    /** 
     * 将字节数组转换成16进制字符串 
     * @param buf 
     * @return 
     */  
    private static String asHex(byte buf[]) {  
        StringBuffer strbuf = new StringBuffer(buf.length * 2);  
        int i;  
        for (i = 0; i < buf.length; i++) {  
            if (((int) buf[i] & 0xff) < 0x10)//小于十前面补零  
                strbuf.append("0");  
            strbuf.append(Long.toString((int) buf[i] & 0xff, 16));  
        }  
        return strbuf.toString();  
    }  
  
    /** 
     * 将16进制字符串转换成字节数组 
     * @param src 
     * @return 
     */  
    private static byte[] asBin(String src) {  
        if (src.length() < 1)  
            return null;  
        byte[] encrypted = new byte[src.length() / 2];  
        for (int i = 0; i < src.length() / 2; i++) {  
            int high = Integer.parseInt(src.substring(i * 2, i * 2 + 1), 16);//取高位字节  
            int low = Integer.parseInt(src.substring(i * 2 + 1, i * 2 + 2), 16);//取低位字节  
            encrypted[i] = (byte) (high * 16 + low);  
        }  
        return encrypted;  
    }  
    
    public static String LCh(String sourString, String cChar, int cLen) {
    	String tReturn = "";
		for (int i = 0; i < cLen; i++) {
			tReturn += cChar;
		}
		tReturn =  tReturn+sourString;
		return tReturn;
	}
	public static String RCh(String sourString, String cChar, int cLen) {
		String tReturn = "";
		for (int i = 0; i < cLen; i++) {
			tReturn += cChar;
		}
		tReturn = sourString + tReturn;
		return tReturn;
	}
}
