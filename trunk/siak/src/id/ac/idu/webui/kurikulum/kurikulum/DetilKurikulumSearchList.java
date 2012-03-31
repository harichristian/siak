package id.ac.idu.webui.kurikulum.kurikulum;

import id.ac.idu.backend.model.Mdetilkurikulum;
import org.apache.log4j.Logger;
import org.zkoss.zk.ui.sys.ComponentsCtrl;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.ListitemRenderer;

import java.io.Serializable;

/**
 * @author <a href="valeo.gumilang@gmail.com">Valeo Gumilang</a>
 * @Date 3/31/12
 * Time: 11:15 AM
 */
public class DetilKurikulumSearchList implements ListitemRenderer, Serializable {
    private static final long serialVersionUID = -1L;
	private static final Logger logger = Logger.getLogger(DetilKurikulumSearchList.class);

    @Override
	public void render(Listitem item, Object data) throws Exception {
		final Mdetilkurikulum detilKurikulum = (Mdetilkurikulum) data;

		Listcell lc = new Listcell(detilKurikulum.getMtbmtkl().getCkdmtk());
		lc.setParent(item);
        lc = new Listcell(detilKurikulum.getMtbmtkl().getCnamamk());
		lc.setParent(item);
        lc = new Listcell(detilKurikulum.getCjenis());
		lc.setParent(item);
        lc = new Listcell(detilKurikulum.getCstatus());
		lc.setParent(item);
        lc = new Listcell(detilKurikulum.getCmun());
		lc.setParent(item);
        lc = new Listcell(detilKurikulum.getCtermsmt());
		lc.setParent(item);
        lc = new Listcell(detilKurikulum.getClintasprodi());
		lc.setParent(item);

        item.setAttribute("data", data);
		ComponentsCtrl.applyForward(item, "onDoubleClick=onDetilKurikulumItem");
	}
}
