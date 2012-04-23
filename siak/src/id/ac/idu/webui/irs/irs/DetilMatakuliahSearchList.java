package id.ac.idu.webui.irs.irs;

import id.ac.idu.backend.model.Tirspasca;
import org.apache.log4j.Logger;
import org.zkoss.zk.ui.sys.ComponentsCtrl;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.ListitemRenderer;

import java.io.Serializable;

/**
 * @author <a href="valeo.gumilang@gmail.com">Valeo Gumilang</a>
 * @Date 4/22/12
 * Time: 3:45 PM
 */
public class DetilMatakuliahSearchList  implements ListitemRenderer, Serializable {
    private static final long serialVersionUID = -1L;
	private static final Logger logger = Logger.getLogger(DetilMatakuliahSearchList.class);

    @Override
	public void render(Listitem item, Object data) throws Exception {
		//final Mdetilkurikulum detilKurikulum = (Mdetilkurikulum) data;
        final Tirspasca detilMatakuliah = (Tirspasca) data;
		Listcell lc = new Listcell(detilMatakuliah.getMtbmtkl().getCkdmtk());
		lc.setParent(item);
        lc = new Listcell(detilMatakuliah.getMtbmtkl().getCnamamk());
		lc.setParent(item);
        lc = new Listcell(detilMatakuliah.getCkelompok());
		lc.setParent(item);
        //lc = new Listcell("");
		//lc.setParent(item);
        String sks = "";
        if(detilMatakuliah.getNsks() != null) {
            sks = detilMatakuliah.getNsks().toString();
        } else {
            if(detilMatakuliah.getMtbmtkl().getNsks() != null) {
                sks = detilMatakuliah.getMtbmtkl().getNsks().toString();
            }
        }
        lc = new Listcell(sks);
		lc.setParent(item);

        item.setAttribute("data", data);
		ComponentsCtrl.applyForward(item, "onDoubleClick=onDetilMatakuliahItem");
	}
}
