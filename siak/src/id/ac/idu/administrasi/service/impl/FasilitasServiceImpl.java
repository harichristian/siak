/**
 * Copyright 2010 the original author or authors.
 *
 * This file is part of Zksample2. http://zksample2.sourceforge.net/
 *
 * Zksample2 is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Zksample2 is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Zksample2.  If not, see <http://www.gnu.org/licenses/gpl.html>.
 */
package id.ac.idu.administrasi.service.impl;


import id.ac.idu.administrasi.dao.FasilitasDAO;
import id.ac.idu.administrasi.service.FasilitasService;
import id.ac.idu.backend.bean.ResultObject;
import id.ac.idu.backend.model.Mfasilitas;

import java.util.List;

/**
 * EN: Service implementation for methods that depends on <b>Mfasilitass</b>.<br>
 * DE: Service Methoden Implementierung betreffend <b>Niederlassungen</b>.<br>
 *
 * @author bbruhns
 * @author sgerth
 */
public class FasilitasServiceImpl implements FasilitasService {

    private FasilitasDAO fasilitasDAO;

    public FasilitasDAO getFasilitasDAO() {
        return fasilitasDAO;
    }

    public void setFasilitasDAO(FasilitasDAO fasilitasDAO) {
        this.fasilitasDAO = fasilitasDAO;
    }

    @Override
    public Mfasilitas getNewMfasilitas() {
        return getFasilitasDAO().getNewMfasilitas();
    }

    @Override
    public Mfasilitas getMfasilitasByID(Long fil_nr) {
        return getFasilitasDAO().getMfasilitasById(fil_nr);
    }

    @Override
    public List<Mfasilitas> getAllMfasilitass() {
        return getFasilitasDAO().getAllMfasilitass();
    }

    @Override
    public void saveOrUpdate(Mfasilitas fasilitas) {
        getFasilitasDAO().saveOrUpdate(fasilitas);
    }

    @Override
    public void delete(Mfasilitas fasilitas) {
        getFasilitasDAO().delete(fasilitas);
    }

    @Override
    public List<Mfasilitas> getMfasilitassLikeCity(String string) {
        return getFasilitasDAO().getMfasilitassLikeCity(string);
    }

    @Override
    public List<Mfasilitas> getMfasilitassLikeName1(String string) {
        return getFasilitasDAO().getMfasilitassLikeName1(string);
    }

    @Override
    public List<Mfasilitas> getMfasilitassLikeNo(String string) {
        return getFasilitasDAO().getMfasilitassLikeNo(string);
    }

    @Override
    public int getCountAllMfasilitass() {
        return getFasilitasDAO().getCountAllMfasilitass();
    }

     @Override
    public ResultObject getAllMfasilitasLikeText(String text, int start, int pageSize) {
        return getFasilitasDAO().getAllMfasilitasLikeText(text, start, pageSize);
    }
}
