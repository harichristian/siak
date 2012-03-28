package id.ac.idu.webui.util;

import com.lowagie.text.ListItem;
import id.ac.idu.util.Codec;
import id.ac.idu.util.CodecInterface;
import org.zkoss.zul.*;

import java.util.ArrayList;
import java.util.List;


/**
 * @author <a href="dbbottle@gmail.com">hermanto</a>
 * @Date 12 Mar 12
 * ==================================================================
 * Copyright (c) 2012  All rights reserved.
 * ==================================================================
 */

public class GFCListModelCtrl  {
    private final static GFCListModelCtrl instance = new GFCListModelCtrl();

    private GFCListModelCtrl() {}

    public static GFCListModelCtrl getInstance() {
        return instance;
    }

    public void setListModel(List _list, Listbox _lbox, Bandbox _box, Object _select) {
        String selidx = String.valueOf(_select);

        for (Object a_list : _list) {
            Listitem lu = _lbox.appendItem(((CodecInterface) a_list).getLabel(), ((CodecInterface) a_list).getValue());
            if (selidx.equals(((CodecInterface) a_list).getValue())) {
                _lbox.setSelectedItem(lu);
                _box.setValue(((CodecInterface) a_list).getLabel());
            }
        }
    }

    public ListModelList setListModel(List _list) {
        List l = new ArrayList();
        l.addAll(_list);

        return (new ListModelList(l));
    }

    public void doCheckBox(Listbox lst, Bandbox bnd, String val) {
        boolean b = false;

        lst.selectItem(null);
        for (Object lmh : lst.getItems()) {
               if(((Listitem) lmh).getValue().equals(val)) {
                   ((Listitem) lmh).setSelected(true);
                   bnd.setValue(((Listitem) lmh).getLabel());
                   b = true;
                   break;
               }
        }

        if(!b) bnd.setValue("");
    }
}
