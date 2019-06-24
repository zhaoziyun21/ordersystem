package com.kssj.base.servlet;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
* @Description: 随机码工具类（servlet）
* @Company:TGRF
* @author:ChenYW
* 
* @date: 2013-10-14 下午02:52:10
* @version V1.0
*/
public class RandomImageServlet extends HttpServlet
{
	private static final long serialVersionUID = 1L;

	private int width = 50;
	private int height = 20;
	private int codeCount = 4;
	private int x = 0;
	private int fontHeight;
	private int codeY;

	char[] codeSequence =
	{
	    '0', '1', '2', '3', '4', '5', '6', '7', '8', '9'
	};

	public void init() throws ServletException
	{
		x = width / (codeCount + 1);
		fontHeight = height - 2;
		codeY = height - 4;
	}

	protected void service(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, java.io.IOException
	{
		BufferedImage buffImg = new BufferedImage(width,height,BufferedImage.TYPE_INT_RGB);
		Graphics2D g = buffImg.createGraphics();
		Random random = new Random();
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, width, height);
		Font font = new Font("Fixedsys", Font.PLAIN, fontHeight);
		g.setFont(font);
		g.setColor(Color.BLACK);
		g.drawRect(0, 0, width - 1, height - 1);

		g.setColor(Color.BLACK);
		for (int i = 0; i < 10; i++)
		{
			int x = random.nextInt(width);
			int y = random.nextInt(height);
			int xl = random.nextInt(12);
			int yl = random.nextInt(12);
			g.drawLine(x, y, x + xl, y + yl);
		}

		StringBuffer randomCode = new StringBuffer();
		int red = 0, green = 0, blue = 0;

		for (int i = 0; i < codeCount; i++)
		{
			String strRand = String.valueOf(codeSequence[random.nextInt(10)]);
			g.setColor(new Color(red, green, blue));
			g.drawString(strRand, (i + 1) * x, codeY);
			randomCode.append(strRand);
		}
		HttpSession session = req.getSession();
//		session.setAttribute(session.getId()+"/RANDOM", randomCode.toString());

		resp.setHeader("Pragma", "no-cache");
		resp.setHeader("Cache-Control", "no-cache");
		resp.setDateHeader("Expires", 0);
		resp.setContentType("image/jpeg");
		ServletOutputStream sos = resp.getOutputStream();
		ImageIO.write(buffImg, "jpeg", sos);
		sos.close();
	}
}