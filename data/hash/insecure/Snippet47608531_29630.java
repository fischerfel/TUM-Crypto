public class PurchaseHelper {


public static final String SKU_ISSUE_PRO_UPGRADE = "pro";
public static final String SKU_ISSUE_PRO_UPGRADE_DISCOUNT = "prodiscount";
public static final String SKU_ISSUE_PRO_UPGRADE_DISCOUNTED = "prodiscounted";
public static final String SKU_ISSUE_PRO_UPGRADE_SUBSCRIPTION= "prosubscription";

private static final String LOG_TAG = PurchaseHelper.class.getSimpleName();
private static final int RC_REQUEST = 10001;
private IabHelper mHelper;


ArrayList<PurchaseItem> mPurchaseItems= new ArrayList<PurchaseItem>();

private static PurchaseHelper mInstance;

public static PurchaseHelper createInstance(){
    if (mInstance==null){
        PurchaseHelper purchaseHelper1 = new PurchaseHelper();
        purchaseHelper1.mPurchaseItems= new ArrayList<PurchaseItem>();
        purchaseHelper1.mPurchaseItems.add(new PurchaseItem(SKU_ISSUE_PRO_UPGRADE,false, Features.UpgradePro));
        purchaseHelper1.mPurchaseItems.add(new PurchaseItem(SKU_ISSUE_PRO_UPGRADE_DISCOUNT,false,Features.UpgradeProDiscount));
        purchaseHelper1.mPurchaseItems.add(new PurchaseItem(SKU_ISSUE_PRO_UPGRADE_DISCOUNTED,false,Features.UpgradeProDiscounted));
        purchaseHelper1.mPurchaseItems.add(new PurchaseItem(SKU_ISSUE_PRO_UPGRADE_SUBSCRIPTION, true,Features.UpgradeProSubscription));

        mInstance =purchaseHelper1;
    }

    return mInstance;
}

public boolean isPurchased(Features feature){
    /*
    //TODO remove
    return true;
    */

    Boolean result = false;

    for (PurchaseItem item:mPurchaseItems) {
        if (item.IsPurchased && item.SKU.equals(SKU_ISSUE_PRO_UPGRADE_DISCOUNT)){
            return true;
        }
        if (item.IsPurchased && item.SKU.equals(SKU_ISSUE_PRO_UPGRADE)){
            return true;
        }
        if (item.IsPurchased && item.SKU.equals(SKU_ISSUE_PRO_UPGRADE_DISCOUNTED)){
            return true;
        }
        if (item.IsPurchased && item.SKU.equals(SKU_ISSUE_PRO_UPGRADE_SUBSCRIPTION)){
            return true;
        }
        if (item.IsPurchased && item.IsFeature==true && item.FeatureName == feature){
            return true;
        }
    }

    return result;

}

public boolean showPurchaseDialog(){
    /*
    //TODO remove
    return false;
    */

    for (PurchaseItem item:mPurchaseItems) {
        if (item.IsPurchased && item.SKU.equals(SKU_ISSUE_PRO_UPGRADE)){
            return false;
        }
        if (item.IsPurchased && item.SKU.equals(SKU_ISSUE_PRO_UPGRADE_DISCOUNT)){
            return false;
        }
        if (item.IsPurchased && item.SKU.equals(SKU_ISSUE_PRO_UPGRADE_DISCOUNTED)){
            return false;
        }
        if (item.IsPurchased && item.SKU.equals(SKU_ISSUE_PRO_UPGRADE_SUBSCRIPTION)){
            return false;
        }
    }
    for (PurchaseItem item:mPurchaseItems) {
        if (item.IsPurchased==false && item.IsFeature==true){
            return true;
        }
    }
    return false;

}

public ArrayList<PurchaseItem> getItems(){
    return mPurchaseItems;
}
public void setPurchases(String sku){
    for (PurchaseItem item:mPurchaseItems) {
        if (item.SKU.equals(sku)){
            item.IsPurchased = true;
        }
    }
}

public void resetPurchases(){
    for (PurchaseItem item:mPurchaseItems) {
        item.IsPurchased = false;
    }
}

public static String md5(String s) {
    try {
        // Create MD5 Hash
        MessageDigest digest = MessageDigest.getInstance("MD5");
        digest.update(s.getBytes());
        byte messageDigest[] = digest.digest();

        // Create Hex String
        StringBuffer hexString = new StringBuffer();
        for (int i=0; i<messageDigest.length; i++)
            hexString.append(Integer.toHexString(0xFF & messageDigest[i]));
        return hexString.toString();

    } catch (NoSuchAlgorithmException e) {
        e.printStackTrace();
    }
    return "";
}
