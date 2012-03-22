package id.ac.idu.backend.service.impl;

import id.ac.idu.backend.dao.*;
import id.ac.idu.backend.service.CommonService;
import id.ac.idu.mankurikulum.dao.MatakuliahDAO;
import id.ac.idu.mankurikulum.dao.SesiDAO;

import java.util.HashMap;
import java.util.Map;

/**
 * Service implementation for methods that depends on <b>all DAO methods</b>.<br>
 * Mainly used for get back the size for every table.
 *
 * @author bbruhns
 * @author sgerth
 */
public class CommonServiceImpl implements CommonService {

    private ArticleDAO articleDAO;
    private BrancheDAO brancheDAO;
    private CustomerDAO customerDAO;
    private OfficeDAO officeDAO;
    private GuestBookDAO guestBookDAO;
    private IpToCountryDAO ipToCountryDAO;
    private OrderDAO orderDAO;
    private OrderpositionDAO orderpositionDAO;
    private UserDAO userDAO;
    private SecUserroleDAO secUserroleDAO;
    private SecRoleDAO secRoleDAO;
    private SecRolegroupDAO secRolegroupDAO;
    private SecGrouprightDAO secGrouprightDAO;
    private SecGroupDAO secGroupDAO;
    private SecRightDAO secRightDAO;
    private SecTypDAO secTypDAO;
    private SecLoginlogDAO secLoginlogDAO;
    private CountryCodeDAO countryCodeDAO;
    private MyCalendarEventDAO calendarEventDAO;
    private YoutubeLinkDAO youtubeLinkDAO;
    private ApplicationNewsDAO applicationNewsDAO;
    private HibernateStatisticsDao hibernateStatisticsDao;
    private SesiDAO sesiDAO;
    private MatakuliahDAO matakuliahDAO;

    public ApplicationNewsDAO getApplicationNewsDAO() {
        return applicationNewsDAO;
    }

    public void setApplicationNewsDAO(ApplicationNewsDAO applicationNewsDAO) {
        this.applicationNewsDAO = applicationNewsDAO;
    }

    public void setHibernateStatisticsDao(HibernateStatisticsDao hibernateStatisticsDao) {
        this.hibernateStatisticsDao = hibernateStatisticsDao;
    }

    public HibernateStatisticsDao getHibernateStatisticsDao() {
        return hibernateStatisticsDao;
    }

    public YoutubeLinkDAO getYoutubeLinkDAO() {
        return youtubeLinkDAO;
    }

    public void setYoutubeLinkDAO(YoutubeLinkDAO youtubeLinkDAO) {
        this.youtubeLinkDAO = youtubeLinkDAO;
    }

    public ArticleDAO getArticleDAO() {
        return articleDAO;
    }

    public void setArticleDAO(ArticleDAO articleDAO) {
        this.articleDAO = articleDAO;
    }

    public BrancheDAO getBrancheDAO() {
        return brancheDAO;
    }

    public void setBrancheDAO(BrancheDAO brancheDAO) {
        this.brancheDAO = brancheDAO;
    }

    public CustomerDAO getCustomerDAO() {
        return customerDAO;
    }

    public void setCustomerDAO(CustomerDAO customerDAO) {
        this.customerDAO = customerDAO;
    }

    public OfficeDAO getOfficeDAO() {
        return officeDAO;
    }

    public void setOfficeDAO(OfficeDAO officeDAO) {
        this.officeDAO = officeDAO;
    }

    public GuestBookDAO getGuestBookDAO() {
        return guestBookDAO;
    }

    public void setGuestBookDAO(GuestBookDAO guestBookDAO) {
        this.guestBookDAO = guestBookDAO;
    }

    public void setIpToCountryDAO(IpToCountryDAO ipToCountryDAO) {
        this.ipToCountryDAO = ipToCountryDAO;
    }

    public IpToCountryDAO getIpToCountryDAO() {
        return ipToCountryDAO;
    }

    public OrderDAO getOrderDAO() {
        return orderDAO;
    }

    public void setOrderDAO(OrderDAO orderDAO) {
        this.orderDAO = orderDAO;
    }

    public OrderpositionDAO getOrderpositionDAO() {
        return orderpositionDAO;
    }

    public void setOrderpositionDAO(OrderpositionDAO orderpositionDAO) {
        this.orderpositionDAO = orderpositionDAO;
    }

    public SecTypDAO getSecTypDAO() {
        return secTypDAO;
    }

    public void setSecTypDAO(SecTypDAO secTypDAO) {
        this.secTypDAO = secTypDAO;
    }

    public SecRightDAO getSecRightDAO() {
        return secRightDAO;
    }

    public void setSecRightDAO(SecRightDAO secRightDAO) {
        this.secRightDAO = secRightDAO;
    }

    public SecGroupDAO getSecGroupDAO() {
        return secGroupDAO;
    }

    public void setSecGroupDAO(SecGroupDAO secGroupDAO) {
        this.secGroupDAO = secGroupDAO;
    }

    public SecGrouprightDAO getSecGrouprightDAO() {
        return secGrouprightDAO;
    }

    public void setSecGrouprightDAO(SecGrouprightDAO secGrouprightDAO) {
        this.secGrouprightDAO = secGrouprightDAO;
    }

    public SecRolegroupDAO getSecRolegroupDAO() {
        return secRolegroupDAO;
    }

    public void setSecRolegroupDAO(SecRolegroupDAO secRolegroupDAO) {
        this.secRolegroupDAO = secRolegroupDAO;
    }

    public SecUserroleDAO getSecUserroleDAO() {
        return secUserroleDAO;
    }

    public void setSecUserroleDAO(SecUserroleDAO secUserroleDAO) {
        this.secUserroleDAO = secUserroleDAO;
    }

    public SecRoleDAO getSecRoleDAO() {
        return secRoleDAO;
    }

    public void setSecRoleDAO(SecRoleDAO secRoleDAO) {
        this.secRoleDAO = secRoleDAO;
    }

    public UserDAO getUserDAO() {
        return userDAO;
    }

    public void setUserDAO(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public void setSecLoginlogDAO(SecLoginlogDAO secLoginlogDAO) {
        this.secLoginlogDAO = secLoginlogDAO;
    }

    public SecLoginlogDAO getSecLoginlogDAO() {
        return secLoginlogDAO;
    }

    public void setCountryCodeDAO(CountryCodeDAO countryCodeDAO) {
        this.countryCodeDAO = countryCodeDAO;
    }

    public CountryCodeDAO getCountryCodeDAO() {
        return countryCodeDAO;
    }

    public void setCalendarEventDAO(MyCalendarEventDAO calendarEventDAO) {
        this.calendarEventDAO = calendarEventDAO;
    }

    public MyCalendarEventDAO getCalendarEventDAO() {
        return calendarEventDAO;
    }

    public SesiDAO getSesiDAO() {
        return sesiDAO;
    }

    public void setSesiDAO(SesiDAO sesiDAO) {
        this.sesiDAO = sesiDAO;
    }

    public MatakuliahDAO getMatakuliahDAO() {
        return matakuliahDAO;
    }

    public void setMatakuliahDAO(MatakuliahDAO matakuliahDAO) {
        this.matakuliahDAO = matakuliahDAO;
    }

    @Override
    public Map<String, Object> getAllTablesRecordCounts() {

        Map<String, Object> map = new HashMap<String, Object>();

        map.put("Customer", getCustomerDAO().getCountAllCustomers());
        map.put("Offices", getOfficeDAO().getCountAllOffices());
        map.put("Branch", getBrancheDAO().getCountAllBranches());
        map.put("Article", getArticleDAO().getCountAllArticles());
        map.put("Order", getOrderDAO().getCountAllOrders());
        map.put("Orderposition", getOrderpositionDAO().getCountAllOrderpositions());
        map.put("GuestBook", getGuestBookDAO().getCountAllGuestBooks());
        map.put("SecGroup", getSecGroupDAO().getCountAllSecGroups());
        map.put("SecGroupright", getSecGrouprightDAO().getCountAllSecGrouprights());
        map.put("SecRight", getSecRightDAO().getCountAllSecRights());
        map.put("SecRole", getSecRoleDAO().getCountAllSecRoles());
        map.put("SecRolegroup", getSecRolegroupDAO().getCountAllSecRolegroups());
        map.put("SecUser", getUserDAO().getCountAllSecUser());
        map.put("SecUserrole", getSecUserroleDAO().getCountAllSecUserroles());
        map.put("SecLoginlog", getSecLoginlogDAO().getCountAllSecLoginlogs());
        map.put("CountryCode", getCountryCodeDAO().getCountAllCountryCodes());
        map.put("IpToCountry", getIpToCountryDAO().getCountAllIpToCountries());
        map.put("CalendarEvents", getCalendarEventDAO().getCountAllCalendarEvents());
        map.put("YouTubeLinks", getYoutubeLinkDAO().getCountAllYoutubeLinks());
        map.put("HibernateStatistics", getHibernateStatisticsDao().getCountAllHibernateStatistics());
        map.put("Sesi", getSesiDAO().getCountAllSesi());
        map.put("Matakuliah", getMatakuliahDAO().getCountAllMatakuliah());
        map.put("ApplicationNews", getApplicationNewsDAO().getCountAllApplicationNews());
        return map;
    }

}
