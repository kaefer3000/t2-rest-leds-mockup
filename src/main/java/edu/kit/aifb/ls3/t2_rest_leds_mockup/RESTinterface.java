package edu.kit.aifb.ls3.t2_rest_leds_mockup;

import java.io.ByteArrayInputStream;
import java.net.URI;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

import javax.servlet.ServletContext;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.GET;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.EntityTag;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.UriInfo;

import org.semanticweb.yars.jaxrs.trailingslash.NotFoundOnTrailingSlash;
import org.semanticweb.yars.jaxrs.trailingslash.RedirectMissingTrailingSlash;
import org.semanticweb.yars.nx.Node;
import org.semanticweb.yars.nx.Resource;
import org.semanticweb.yars.nx.namespace.RDF;

import edu.kit.aifb.ls3.t2_rest_leds_mockup.DerServletContextListener.ImagePanel;
import edu.kit.aifb.ls3.t2_rest_leds_mockup.DerServletContextListener.TesselLedMockupAttributes;

@Path("/")
public class RESTinterface {

	static final Logger _log = Logger.getLogger(RESTinterface.class.getName());
	
	static class SOSA {
		private static final String NS = "http://www.w3.org/ns/sosa/";
		public static final Resource PLATFORM = new Resource("<"+NS+"Platform" + ">", true);
		public static final Resource HOSTS = new Resource("<"+NS+"hosts" + ">", true);
		public static final Resource ACTUABLE_PROPERTY = new Resource("<" + NS + "ActuatableProperty" + ">", true);
	}
	
	static class SSN {
		private static final String NS = "http://www.w3.org/ns/ssn/";
		public static final Resource HAS_PROPERTY = new Resource("<" + NS + "hasProperty" + ">", true);
	}
	
	static class SAREF {
		private static final String NS = "https://w3id.org/saref#";
		public static final Resource ON = new Resource("<" + NS + "On" + ">", true);
		public static final Resource OFF = new Resource("<" + NS + "Off" + ">", true);
		public static final Resource LIGHTING_DEVICE = new Resource("<" + NS + "LightingDevice" + ">", true);
		public static final Resource HAS_STATE = new Resource("<" + NS + "hasState" + ">", true);		
	}

	@Context
	ServletContext _ctx;
	
	@GET
	public Response getTessel(@Context javax.ws.rs.core.UriInfo uriinfo, @Context Request request) {
		
		List<Node[]> l = new LinkedList<Node[]>();
		
		URI absPath = uriinfo.getAbsolutePath();
		
		Resource thisRes = new Resource("<" + absPath + "#tessel2" + ">", true);
		
		l.add(new Node[] {  thisRes, RDF.TYPE, SOSA.PLATFORM});
		l.add(new Node[] {  thisRes, SOSA.HOSTS, new Resource("<" + absPath + "/leds/#bar" + ">")});
		
		EntityTag etag = new EntityTag("W/" + Integer.toHexString(l.hashCode()));

		ResponseBuilder builder = request.evaluatePreconditions(etag);
		if (builder != null)
			return builder.build();
		else {
			return Response.ok(new GenericEntity<Iterable<Node[]>>(l) {
			}).tag(etag).build();
		}
	}
	
	@GET
	@Path("/leds/")
	@RedirectMissingTrailingSlash
	public Response getLeds(@Context UriInfo uriinfo, @Context Request request) {
		
		List<Node[]> l = new LinkedList<Node[]>();
		
		URI absPath = uriinfo.getAbsolutePath();
		
		Resource thisRes = new Resource("<" + absPath + "#bar" + ">", true);
		
		l.add(new Node[] {  thisRes, RDF.TYPE, SOSA.PLATFORM});
		l.add(new Node[] {  thisRes, SOSA.HOSTS, new Resource("<" + absPath + "/0#led" + ">", true)});
		l.add(new Node[] {  thisRes, SOSA.HOSTS, new Resource("<" + absPath + "/1#led" + ">", true)});
		
		EntityTag etag = new EntityTag("W/" + Integer.toHexString(l.hashCode()));

		ResponseBuilder builder = request.evaluatePreconditions(etag);
		if (builder != null)
			return builder.build();
		else {
			return Response.ok(new GenericEntity<Iterable<Node[]>>(l) {
			}).tag(etag).build();
		}
	}
	
	@GET
	@Path("/leds/0")
	@NotFoundOnTrailingSlash
	public Response getLed0(@Context UriInfo uriinfo, @Context Request request) {
		
		List<Node[]> l = new LinkedList<Node[]>();
		
		URI absPath = uriinfo.getAbsolutePath();
		
		Resource thisRes = new Resource("<" + absPath + "#led" + ">", true);
//		Resource lightRes = new Resource("<" + absPath + "#light" + ">", true);
		
		l.add(new Node[] { thisRes, RDF.TYPE, SAREF.LIGHTING_DEVICE});
//		l.add(new Node[] { thisRes, SSN.HAS_PROPERTY, lightRes});
//		l.add(new Node[] { lightRes, RDF.TYPE, SOSA.ACTUABLE_PROPERTY });
		Object o = _ctx.getAttribute(TesselLedMockupAttributes.STATE.name());
		if (o.equals(TesselLedMockupAttributes.NULL)|| o.equals(TesselLedMockupAttributes.ZWEI)) {
//			l.add(new Node[] { lightRes, RDF.VALUE, SAREF.OFF});
			l.add(new Node[] { thisRes, SAREF.HAS_STATE, SAREF.OFF});
		}
		else {
//			l.add(new Node[] { lightRes, RDF.VALUE, SAREF.ON});
			l.add(new Node[] { thisRes, SAREF.HAS_STATE, SAREF.ON});	
		}
		
		EntityTag etag = new EntityTag("W/" + Integer.toHexString(l.hashCode()));

		ResponseBuilder builder = request.evaluatePreconditions(etag);
		if (builder != null)
			return builder.build();
		else {
			return Response.ok(new GenericEntity<Iterable<Node[]>>(l) {
			}).tag(etag).build();
		}
	}
	
	@GET
	@Path("/leds/1")
	@NotFoundOnTrailingSlash
	public Response getLed1(@Context UriInfo uriinfo, @Context Request request) {
		
		List<Node[]> l = new LinkedList<Node[]>();
		
		URI absPath = uriinfo.getAbsolutePath();
		
		Resource thisRes = new Resource("<" + absPath + "#led" + ">", true);
//		Resource lightRes = new Resource("<" + absPath + "#light" + ">", true);
		
		l.add(new Node[] { thisRes, RDF.TYPE, SAREF.LIGHTING_DEVICE});
//		l.add(new Node[] { thisRes, SSN.HAS_PROPERTY, lightRes});
//		l.add(new Node[] { lightRes, RDF.TYPE, SOSA.ACTUABLE_PROPERTY });
		Object o = _ctx.getAttribute(TesselLedMockupAttributes.STATE.name());
		if (o.equals(TesselLedMockupAttributes.NULL)|| o.equals(TesselLedMockupAttributes.EINS)) {
//			l.add(new Node[] { lightRes, RDF.VALUE, SAREF.OFF});
			l.add(new Node[] { thisRes, SAREF.HAS_STATE, SAREF.OFF});
		}
		else {
//			l.add(new Node[] { lightRes, RDF.VALUE, SAREF.ON});
			l.add(new Node[] { thisRes, SAREF.HAS_STATE, SAREF.ON});
		}
		EntityTag etag = new EntityTag("W/" + Integer.toHexString(l.hashCode()));

		ResponseBuilder builder = request.evaluatePreconditions(etag);
		if (builder != null)
			return builder.build();
		else {
			return Response.ok(new GenericEntity<Iterable<Node[]>>(l) {
			}).tag(etag).build();
		}
	}


	@PUT
	@Path("/leds/0")
	@NotFoundOnTrailingSlash
	public Response putLed0(Iterable<Node[]> input) {

		ImagePanel ip = (ImagePanel) _ctx
				.getAttribute(DerServletContextListener.TesselLedMockupAttributes.CONTENTPANEL.name());

		for (Node[] nx : input) {
			if (nx[1].equals(SAREF.HAS_STATE)) {
				try {
					if (nx[2].equals(SAREF.ON)) {
						if ((_ctx.getAttribute(TesselLedMockupAttributes.STATE.name()))
								.equals(TesselLedMockupAttributes.EINS)
								|| (_ctx.getAttribute(TesselLedMockupAttributes.STATE.name()))
										.equals(TesselLedMockupAttributes.DREI)) {
							return Response.noContent().build();
						} else if ((_ctx.getAttribute(TesselLedMockupAttributes.STATE.name()))
								.equals(TesselLedMockupAttributes.ZWEI)) {
							ip.render(new ByteArrayInputStream(((byte[][]) _ctx.getAttribute(
									DerServletContextListener.TesselLedMockupAttributes.IMAGES.name()))[3]));
							_ctx.setAttribute(TesselLedMockupAttributes.STATE.name(),
									(TesselLedMockupAttributes.DREI));
							return Response.noContent().build();
						} else {
							ip.render(new ByteArrayInputStream(((byte[][]) _ctx.getAttribute(
									DerServletContextListener.TesselLedMockupAttributes.IMAGES.name()))[1]));
							_ctx.setAttribute(TesselLedMockupAttributes.STATE.name(),
									(TesselLedMockupAttributes.EINS));
							return Response.noContent().build();
						}
					} else if (nx[2].equals(SAREF.OFF)) {
						if ((_ctx.getAttribute(TesselLedMockupAttributes.STATE.name()))
								.equals(TesselLedMockupAttributes.NULL)
								|| (_ctx.getAttribute(TesselLedMockupAttributes.STATE.name()))
										.equals(TesselLedMockupAttributes.ZWEI)) {
							return Response.noContent().build();
						} else if ((_ctx.getAttribute(TesselLedMockupAttributes.STATE.name()))
								.equals(TesselLedMockupAttributes.EINS)) {
							ip.render(new ByteArrayInputStream(((byte[][]) _ctx.getAttribute(
									DerServletContextListener.TesselLedMockupAttributes.IMAGES.name()))[0]));
							_ctx.setAttribute(TesselLedMockupAttributes.STATE.name(),
									(TesselLedMockupAttributes.NULL));
							return Response.noContent().build();
						} else {
							ip.render(new ByteArrayInputStream(((byte[][]) _ctx.getAttribute(
									DerServletContextListener.TesselLedMockupAttributes.IMAGES.name()))[2]));
							_ctx.setAttribute(TesselLedMockupAttributes.STATE.name(),
									(TesselLedMockupAttributes.ZWEI));
							return Response.noContent().build();
						}
					} else {
						throw new BadRequestException();
					}
				} catch (Exception e) {
					throw new InternalServerErrorException(e);
				}
			}
		}
		throw new BadRequestException();
	}
	@PUT
	@Path("/leds/1")
	@NotFoundOnTrailingSlash
	public Response putLed1(Iterable<Node[]> input) {

		ImagePanel ip = (ImagePanel) _ctx
				.getAttribute(DerServletContextListener.TesselLedMockupAttributes.CONTENTPANEL.name());

		for (Node[] nx : input) {
			if (nx[1].equals(SAREF.HAS_STATE)) {
				try {
					if (nx[2].equals(SAREF.ON)) {
						if ((_ctx.getAttribute(TesselLedMockupAttributes.STATE.name()))
								.equals(TesselLedMockupAttributes.ZWEI)
								|| (_ctx.getAttribute(TesselLedMockupAttributes.STATE.name()))
										.equals(TesselLedMockupAttributes.DREI)) {
							return Response.noContent().build();
						} else if ((_ctx.getAttribute(TesselLedMockupAttributes.STATE.name()))
								.equals(TesselLedMockupAttributes.NULL)) {
							ip.render(new ByteArrayInputStream(((byte[][]) _ctx.getAttribute(
									DerServletContextListener.TesselLedMockupAttributes.IMAGES.name()))[2]));
							_ctx.setAttribute(TesselLedMockupAttributes.STATE.name(),
									(TesselLedMockupAttributes.ZWEI));
							return Response.noContent().build();
						} else {
							ip.render(new ByteArrayInputStream(((byte[][]) _ctx.getAttribute(
									DerServletContextListener.TesselLedMockupAttributes.IMAGES.name()))[3]));
							_ctx.setAttribute(TesselLedMockupAttributes.STATE.name(),
									(TesselLedMockupAttributes.DREI));
							return Response.noContent().build();
						}
					} else if (nx[2].equals(SAREF.OFF)) {
						if ((_ctx.getAttribute(TesselLedMockupAttributes.STATE.name()))
								.equals(TesselLedMockupAttributes.NULL)
								|| (_ctx.getAttribute(TesselLedMockupAttributes.STATE.name()))
										.equals(TesselLedMockupAttributes.EINS)) {
							return Response.noContent().build();
						} else if ((_ctx.getAttribute(TesselLedMockupAttributes.STATE.name()))
								.equals(TesselLedMockupAttributes.ZWEI)) {
							ip.render(new ByteArrayInputStream(((byte[][]) _ctx.getAttribute(
									DerServletContextListener.TesselLedMockupAttributes.IMAGES.name()))[0]));
							_ctx.setAttribute(TesselLedMockupAttributes.STATE.name(),
									(TesselLedMockupAttributes.NULL));
							return Response.noContent().build();
						} else {
							ip.render(new ByteArrayInputStream(((byte[][]) _ctx.getAttribute(
									DerServletContextListener.TesselLedMockupAttributes.IMAGES.name()))[1]));
							_ctx.setAttribute(TesselLedMockupAttributes.STATE.name(),
									(TesselLedMockupAttributes.EINS));
							return Response.noContent().build();
						}
					} else {
						throw new BadRequestException();
					}
				} catch (Exception e) {
					throw new InternalServerErrorException(e);
				}
			}
		}
		throw new BadRequestException();
	}
}