package edu.kit.aifb.ls3.t2_rest_leds_mockup;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.logging.Logger;

import javax.imageio.ImageIO;
import javax.servlet.FilterRegistration;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.ServletRegistration;
import javax.servlet.annotation.WebListener;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.ws.rs.HttpMethod;

@WebListener
public class DerServletContextListener implements ServletContextListener {

	final static Logger _log = Logger.getLogger(DerServletContextListener.class.getName());

	ServletContext _ctx;
	
	final static String[] dateinamen = new String[] { "tessel_00.png", "tessel_01.png", "tessel_10.png", "tessel_11.png" };
	final static byte[][] images;
	
	static {
		// Loading the files into byte arrays.
		images = new byte[dateinamen.length][];
		ClassLoader classloader = Thread.currentThread().getContextClassLoader();

		ArrayList<Byte> l = new ArrayList<Byte>();
		InputStream is;
		int b;

		for (int j = 0; j < dateinamen.length; ++j) {
			is = classloader.getResourceAsStream(dateinamen[j]);
			l.clear();
			b = -1;
			try {
				while ((b = is.read()) > -1) {
					l.add((byte) b);
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			images[j] = new byte[l.size()];
			for (int i = 0; i < l.size() - 1; ++i) {
				images[j][i] = l.get(i).byteValue();
			}
		}
	}

	enum TesselLedMockupAttributes {
		WINDOWFRAME, CONTENTPANEL, STATE, IMAGES, NULL, EINS, ZWEI, DREI
	}

	public class ImagePanel extends JPanel {

		private static final long serialVersionUID = -6041044479667736419L;

		private BufferedImage _image;

		public ImagePanel() {

			_image = new BufferedImage(500, 500, BufferedImage.TYPE_INT_ARGB);
			
			try {
				render(new ByteArrayInputStream(images[0]));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			g.drawImage(_image, 0, 0, null);
		}

		public boolean render(InputStream is) throws IOException {
			BufferedImage bi = ImageIO.read(is);

			if (bi == null)
				return false;
			else {
				_image = bi;
				repaint();
				return true;
			}
		}
	}

	@Override
	public void contextInitialized(ServletContextEvent sce) {

		_ctx = sce.getServletContext();
		
		// Register Servletinhalt
		ServletRegistration sr = _ctx.addServlet("A Tessel mockup server. Provides a REST interface to two LEDs.",
				org.glassfish.jersey.servlet.ServletContainer.class);
		sr.addMapping("/*");
		sr.setInitParameter(org.glassfish.jersey.server.ServerProperties.PROVIDER_PACKAGES,
				this.getClass().getPackage().getName() + ","
						+ org.semanticweb.yars.jaxrs.JerseyAutoDiscoverable.class.getPackage().getName());

		FilterRegistration fr;
		// Register and configure filter to handle CORS requests
		fr = _ctx.addFilter("cross-origin", org.eclipse.jetty.servlets.CrossOriginFilter.class.getName());
		fr.setInitParameter(org.eclipse.jetty.servlets.CrossOriginFilter.ALLOWED_METHODS_PARAM,
				HttpMethod.GET + "," + HttpMethod.PUT + "," + HttpMethod.POST + "," + HttpMethod.DELETE);
		fr.addMappingForUrlPatterns(null, true, "/*");
		
		JFrame frame = new JFrame();
		frame.setSize(600, 600);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JPanel panel = new ImagePanel();
		frame.add(panel);
		frame.setVisible(true);
		
		_ctx.setAttribute(TesselLedMockupAttributes.CONTENTPANEL.name(), panel);
		_ctx.setAttribute(TesselLedMockupAttributes.WINDOWFRAME.name(), frame);
		_ctx.setAttribute(TesselLedMockupAttributes.IMAGES.name(), images);
		
		_ctx.setAttribute(TesselLedMockupAttributes.STATE.name(), TesselLedMockupAttributes.NULL);
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		JFrame f = (JFrame) _ctx.getAttribute(TesselLedMockupAttributes.WINDOWFRAME.name());
		f.setVisible(false);
	}

}
