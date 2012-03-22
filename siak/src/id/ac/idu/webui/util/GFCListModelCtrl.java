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

        for(int i=0; i<_list.size(); i++) {
            Listitem lu = _lbox.appendItem(((CodecInterface) _list.get(i)).getLabel(), ((CodecInterface) _list.get(i)).getValue());
            if(selidx.equals(((CodecInterface) _list.get(i)).getValue())) {
                _lbox.setSelectedItem(lu);
                _box.setValue(((CodecInterface) _list.get(i)).getLabel());
            }
        }
    }

    public ListModelList setListModel(List _list) {
        List l = new ArrayList();
        l.addAll(_list);

        return (new ListModelList(l));
    }
}
