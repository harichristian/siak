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
package id.ac.idu.services.report.service.impl;

import id.ac.idu.backend.dao.*;
import id.ac.idu.backend.model.Customer;
import id.ac.idu.backend.model.Order;
import id.ac.idu.backend.model.Orderposition;
import id.ac.idu.backend.service.CustomerService;
import id.ac.idu.backend.service.OrderService;
import id.ac.idu.services.report.service.ReportService;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;
import org.apache.log4j.Logger;

import java.io.InputStream;
import java.util.*;

/**
 * Implementation of the service methods.
 *
 * @author bbruhns
 * @author sgerth
 */
public class ReportServiceImpl implements ReportService {

    private transient OrderDAO orderDAO;
    private transient OrderpositionDAO orderpositionDAO;
    private transient BrancheDAO brancheDAO;
    private transient CustomerDAO customerDAO;
    private transient OfficeDAO officeDAO;
    private transient OrderService orderService;
    private transient CustomerService customerService;

    @Override
    public JRDataSource getBeanCollectionByAuftrag(Order anOrder) {

        // init all needed lists
        final ArrayList<Customer> customerList = new ArrayList<Customer>();
        final Set<Orderposition> orderpositionsSet = new HashSet<Orderposition>();
        final Set<Order> orderSet = new HashSet<Order>();

        // get the customer for this order
        final Customer customer = getCustomerService().getCustomerByOrder(anOrder);

        /* fill the orderPositionList */
        orderpositionsSet.addAll(getOrderService().getOrderpositionsByOrder(anOrder));

        // plug all together
        anOrder.setOrderpositions(orderpositionsSet);
        /* fill the orderList */
        orderSet.add(anOrder);

        customer.setOrders(orderSet);
        customerList.add(customer);

        return new JRBeanCollectionDataSource(customerList);
    }

    /*
      * (non-Javadoc)
      *
      * @see
      * id.ac.idu.backend.service.ReportService#printAuftragsPositionen(id.ac.
      * daibutsu.backend.model.Auftrag, java.util.HashMap)
      */
    @Override
    public void printAuftragsPositionen(Order auftrag, HashMap repParams) {

        try {

            final InputStream inputStream = getClass().getResourceAsStream("/id/ac/forsthaus/webui/reports/AuftragDetailsPojo_Report.jrxml");

            /* Liste mit Daten füllen */
            final List<Orderposition> result = getOrderService().getOrderpositionsByOrder(auftrag);

            /* DataSource mit der Liste erstellen */
            final JRBeanCollectionDataSource datasource = new JRBeanCollectionDataSource(result);

            final JasperDesign jasperDesign = JRXmlLoader.load(inputStream);
            final JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);

            final JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, repParams, datasource);
            JasperViewer.viewReport(jasperPrint, false);
        } catch (final JRException ex) {
            final String connectMsg = "JasperReports: Could not create the report " + ex.getMessage() + " " + ex.getLocalizedMessage();
            Logger.getLogger(getClass()).error(connectMsg, ex);
        } catch (final Exception ex) {
            final String connectMsg = "Could not create the report " + ex.getMessage() + " " + ex.getLocalizedMessage();
            Logger.getLogger(getClass()).error(connectMsg, ex);
        }
    }

    @Override
    public void compileReport(String aReportPathName) {
        try {
            final InputStream inputStream = getClass().getResourceAsStream(aReportPathName);
            final JasperDesign jasperDesign = JRXmlLoader.load(inputStream);
            final JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);

        } catch (final Exception ex) {
            final String connectMsg = "JasperReports: Could not create the report " + ex.getMessage() + " " + ex.getLocalizedMessage();
            Logger.getLogger(getClass()).error(connectMsg, ex);
        }

    }

    // ++++++++++++++++++++++++++++++++++++++++++++++++++++++//
    // ++++++++++++++++++ getter / setter +++++++++++++++++++//
    // ++++++++++++++++++++++++++++++++++++++++++++++++++++++//

    public BrancheDAO getBrancheDAO() {
        return this.brancheDAO;
    }

    public void setBrancheDAO(BrancheDAO brancheDAO) {
        this.brancheDAO = brancheDAO;
    }

    public void setCustomerDAO(CustomerDAO customerDAO) {
        this.customerDAO = customerDAO;
    }

    public CustomerDAO getCustomerDAO() {
        return this.customerDAO;
    }

    public void setOfficeDAO(OfficeDAO officeDAO) {
        this.officeDAO = officeDAO;
    }

    public OfficeDAO getOfficeDAO() {
        return this.officeDAO;
    }

    public void setOrderDAO(OrderDAO orderDAO) {
        this.orderDAO = orderDAO;
    }

    public OrderDAO getOrderDAO() {
        return this.orderDAO;
    }

    public void setOrderpositionDAO(OrderpositionDAO orderpositionDAO) {
        this.orderpositionDAO = orderpositionDAO;
    }

    public OrderpositionDAO getOrderpositionDAO() {
        return this.orderpositionDAO;
    }

    public void setOrderService(OrderService orderService) {
        this.orderService = orderService;
    }

    public OrderService getOrderService() {
        return this.orderService;
    }

    public void setCustomerService(CustomerService customerService) {
        this.customerService = customerService;
    }

    public CustomerService getCustomerService() {
        return customerService;
    }

}
