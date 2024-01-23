package com.javinindia.nismwa.constant;

public class Constants {
    public static int COUNT = 0;
    /*public static final String NEW_BASE_URL = "http://hnwkart.com/Nismwa/nismwa_api/index.php/Member/";
    public static final String NEW_BASE_URL_MASTER = "http://hnwkart.com/Nismwa/nismwa_api/index.php/Masters/";*/
    public static final String NEW_BASE_URL = "http://japps.co.in/nismwah_test/nismwa_api/index.php/Member/";
    public static final String NEW_BASE_URL_MASTER = "http://japps.co.in/nismwah_test/nismwa_api/index.php/Masters/";
    // public static final String NEW_BASE_URL = "http://wonderdelights.com/delights/";
    public static final String TEST_URL = "http://hnwkart.com/isc_api/index.php/IseCandidate/";

    public static final String LOGIN_URL = Constants.NEW_BASE_URL + "memberLogin";
    public static final String LOGOUT_URL = Constants.NEW_BASE_URL + "logOut";
    public static final String MEMBER_PROFILE_URL = Constants.NEW_BASE_URL + "ownerProfile";
    public static final String MEMBER_EDIT_PIC_URL = Constants.NEW_BASE_URL + "editProfilePicOwner";
    public static final String MEMBER_EDIT_PROFILE_URL = Constants.NEW_BASE_URL + "editProfileOwner";
    public static final String MEMBERS_LIST_URL = Constants.NEW_BASE_URL + "getAllFirmOwners";
    public static final String BROKERS_LIST_URL = Constants.NEW_BASE_URL + "MemberListByType";
    public static final String MANAGEMENT_LIST_URL = Constants.NEW_BASE_URL + "getAllManagementMembers";
    public static final String MANAGEMENT_LIST_BY_PRIORITY_URL = Constants.NEW_BASE_URL + "getAllManagementMembersByPriority";
    public static final String EVENT_LIST_URL = Constants.NEW_BASE_URL + "getAllEvent";
    public static final String NEWS_LIST_URL = Constants.NEW_BASE_URL + "getAllNews";
    public static final String ADD_SUGGESTION_URL = Constants.NEW_BASE_URL + "addComplaint";
    public static final String EDIT_DEAL_URL = Constants.NEW_BASE_URL + "editDealByMember";

    public static final String USEFULL_LIST_URL = Constants.NEW_BASE_URL + "getUsefulInformation";
    public static final String CALENDER_LIST_URL = Constants.NEW_BASE_URL + "holidayCalendar";
    public static final String CONVERSION_LIST_URL = Constants.NEW_BASE_URL + "conversionList";
    public static final String NOTIFICATION_LIST_URL = Constants.NEW_BASE_URL + "getNotificationList";
    public static final String NOTIFICATION_COUNT_URL = Constants.NEW_BASE_URL + "getCountList";
    public static final String NOTIFICATION_DELETE_URL = Constants.NEW_BASE_URL + "delete_notification";
    public static final String SUGGESTION_LIST_URL = Constants.NEW_BASE_URL + "getMySuggetion";
    public static final String SEARCH_ALPHA_URL = Constants.NEW_BASE_URL + "getAllFirmOwnersBySearch";



    public static final String COMMENT_LIST_URL = Constants.NEW_BASE_URL + "getAllCommentsByPostId";
    public static final String COMMENT_INSERT_URL = Constants.NEW_BASE_URL + "addWallComments";
    public static final String COMMENT_EDIT_URL = Constants.NEW_BASE_URL + "editWallComments";



    public static final String FORGET_PASSWORD_URL = Constants.NEW_BASE_URL + "companyRecruiterForgotPassword";
    public static final String CHANGE_PASSWORD_URL = Constants.NEW_BASE_URL + "companyRecruiterChangePassword";


    public static final String CITY_LIST_URL = Constants.TEST_URL + "cityList";
    public static final String STATE_LIST_URL = Constants.TEST_URL + "stateList";
    public static final String CONVERSION_URL = Constants.TEST_URL + "idConversion";
    public static final String FEEDBACK_URL = Constants.TEST_URL + "Feedback";
    public static final String ADS_URL = Constants.NEW_BASE_URL + "ads";
    public static final String MAIN_ADS_URL = Constants.NEW_BASE_URL + "mainAds";

    //----------------------master APIs ----------------------------------//
    public static final String DEAL_TYPE_MASTER = Constants.NEW_BASE_URL_MASTER + "getAllDeals";
    public static final String MEMBER_TYPE_MASTER = Constants.NEW_BASE_URL_MASTER + "getAllMemberType";
    public static final String MEMBER_USEFUL_MASTER = Constants.NEW_BASE_URL_MASTER + "getAllUsefulMaster";
    public static final String PROFESSION_LIST_URL = Constants.NEW_BASE_URL_MASTER + "getAllOwnerProfession";
    public static final String VEHICLE_TYPE_LIST_URL = Constants.NEW_BASE_URL_MASTER + "getAllvehicleType";
    public static final String RELATION_TYPE_LIST_URL = Constants.NEW_BASE_URL_MASTER + "getAllFamilyRelation";
    public static final String BLOOD_MASTER_NEW = Constants.NEW_BASE_URL_MASTER + "getAllBloodGroup";


    //----------------------------------blood group----------------------------------//
    public static final String NEW_BASE_BLOOD_URL = "http://japps.co.in/nismwah_test/nismwa_api/index.php/BloodApi/";
    //http://dwarka.online/nismwah_test/nismwa_api
    public static final String BLOOD_MASTER = Constants.NEW_BASE_BLOOD_URL + "getBloodGroup";
    public static final String DONATE_BLOOD = Constants.NEW_BASE_BLOOD_URL + "AddBlood";
    public static final String NEED_BLOOD = Constants.NEW_BASE_BLOOD_URL + "AddBloodNeed";
    public static final String BLOOD_DONOR_LIST = Constants.NEW_BASE_URL + "getAllDoner";

    public static final String APP_SETTINGS = "APP_SETTINGS";
    public static final String USERNAME = "username";
    public static final String EMAIL = "email";
    public static final String GENDER = "gender";
    public static final String ID = "id";
    public static final String PHONE = "phone";
    public static final String ANDROID_ID = "android_id";
    public static final String USER_ID = "user_id";

    public static final String FRG_ABOUT = "AboutFragment";
    public static final String FRG_ADD_WALL = "AddWallFragment";
    public static final String FRG_CALCULATOR = "CalculatorFragment";
    public static final String FRG_CALENDER = "CalenderFragment";
    public static final String FRG_ADD_COMPLAINT = "AddComplaintFragment";
    public static final String FRG_CHANGE_PASSWORD = "ChangePasswordFragment";
    public static final String FRG_CHECK_INTERNET = "CheckConnectionFragment";
    public static final String FRG_EDIT_PROFILE = "EditProfileFragment";
    public static final String FRG_COMMENT_LIST = "CommentFragment";
    public static final String FRG_EVENT = "EventFragment";
    public static final String FRG_EVENT_TAB = "EventTabFragment";
    public static final String FRG_FEEDBACK = "FeedbackFragment";
    public static final String FRG_FILTER = "FilterMemberFragment";
    public static final String FRG_FORGOT_PASSWORD = "ForgotPasswordFragment";
    public static final String FRG_FULLIMAGE = "FullSizeImageFragment";
    public static final String FRG_HELP_SUPPORT = "HelpSupportFragment";
    public static final String FRG_HOME_PAGE = "HomePageFragment";
    public static final String FRG_LOGIN = "LoginFragment";
    public static final String FRG_MANAGEMENT_LIST = "ManagementListFragment";
    public static final String FRG_MANAGEMENT_ONE_LIST = "ManagementOneFragment";
    public static final String FRG_MANAGEMENT_XBLOCK_LIST = "ManagementXblockFragment";
    public static final String FRG_MANAGEMENT_YBLOCK_LIST = "ManagementYblockFragment";
    public static final String FRG_MANAGEMENT_ZBLOCK_LIST = "ManagementXblockFragment";
    public static final String FRG_MANAGEMENT_NOMINATED_LIST = "ManagementNominatedFragment";
    public static final String FRG_MANAGEMENT_ARBITRATION_LIST = "MemberArbitrationFragment";
    public static final String FRG_MEMBER_LIST = "MemberListFragment";
    public static final String FRG_MEMBER_PROFILE = "MemberProfileFragment";
    public static final String FRG_NEWS = "NewsFragment";
    public static final String FRG_NOTIFICATION = "NotificationListFragment";
    public static final String FRG_OTHER_MEMBER_PROFILE = "OtherMemberProfile";
    public static final String FRG_MY_SUGGESTION_LIST = "MySuggestionFragment";
    public static final String FRG_OTHER_MEMBER_LIST = "OtpFragment";
    public static final String FRG_OTP = "OtpFragment";
    public static final String FRG_SETTINGS = "SettingFragment";
    public static final String FRG_USEFUL = "UsefullFragment";
}
