package id.ac.idu.webui.irs.jadwalkuliah;

import id.ac.idu.backend.model.Tjadkulmaster;
import org.apache.log4j.Logger;
import org.zkoss.zk.ui.sys.ComponentsCtrl;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.ListitemRenderer;

import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: Hari Christian
 * Date: 11/03/12
 * Time: 22:18
 * To change this template use File | Settings | File Templates.
 */
public class JadkulmasterListItemRenderer implements ListitemRenderer, Serializable {

    private static final long serialVersionUID = 1L;
    private static final Logger logger = Logger.getLogger(JadkulmasterListItemRenderer.class);

    @Override
    public void render(Listitem item, Object data) throws Exception {

        final Tjadkulmaster obj = (Tjadkulmaster) data;

        Listcell lc = new Listcell(obj.getMsekolah().getCnamaSekolah());
        lc.setParent(item);
        lc = new Listcell(obj.getMprodi().getCnmprogst());
        lc.setParent(item);
        lc = new Listcell(obj.getMpegawai1().getCnama());
        lc.setParent(item);
        lc = new Listcell(obj.getMpegawai2().getCnama());
        lc.setParent(item);
        lc = new Listcell(obj.getMtbmtkl().getCnamamk());
        lc.setParent(item);
        lc = new Listcell(obj.getCterm());
        lc.setParent(item);
        lc = new Listcell(obj.getCkelompok());
        lc.setParent(item);
        lc = new Listcell(obj.getCthajar());
        lc.setParent(item);
        lc = new Listcell(obj.getCsmt());
        lc.setParent(item);
        lc = new Listcell(String.valueOf(obj.getNsks()));
        lc.setParent(item);
        // lc = new Listcell();
        // Image img = new Image();
        // img.setSrc("/images/icons/page_detail.gif");
        // lc.appendChild(img);
        // lc.setParent(item);

        item.setAttribute("data", data);
        // ComponentsCtrl.applyForward(img, "onClick=onImageClicked");
        // ComponentsCtrl.applyForward(item, "onClick=onClicked");
        ComponentsCtrl.applyForward(item, "onDoubleClick=onDoubleClickedTjadkulmasterItem");

    }

}
