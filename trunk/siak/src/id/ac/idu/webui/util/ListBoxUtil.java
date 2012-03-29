package id.ac.idu.webui.util;

import org.zkoss.zul.Listbox;

/**
 * Created by IntelliJ IDEA.
 * User: siak
 * Date: 3/29/12
 * Time: 10:05 AM
 * To change this template use File | Settings | File Templates.
 */
public class ListBoxUtil {
    //reset list
    public static void resetList(Listbox listbox){
            while (listbox.getItemCount() > 0) {
            listbox.removeItemAt(0);
        }
    }
}
