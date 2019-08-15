public class JSONServices {
    public static final Boolean CallService = true;
    JSONHelper json = new JSONHelper();
    public String MissedQueriesCount = "0";

    // Function to Login A User
    public SW_Login Login(String UserId, String Password) {
        SW_Login loginwrapper = new SW_Login();
        JSONObject jObject = new JSONObject();
        if (CallService == true) {
            jObject = json.getHttpJson(JSONServiceURL.Login(UserId, Password));
        } else // For Test Purpose
        {
            String jsonString = "{\"Service\":\"Login\",\"ResultSet\":[{\"Status\":\"1\",\"UserId\":\"21\",\"Token\":\"VJgueUxYCNaN6JGk\",\"Errorcode\":\"\",\"Errordesc\":\"\"}]}";
            try {
                jObject = new JSONObject(jsonString);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        JSONArray retData = GetJSONArray(jObject, "ResultSet");

        loginwrapper.Status = GetJSONElement(retData, "Status", 0);
        loginwrapper.UserId = GetJSONElement(retData, "UserId", 0);
        loginwrapper.Token = GetJSONElement(retData, "Token", 0);
        loginwrapper.ErrCode = GetJSONElement(retData, "Errorcode", 0);
        loginwrapper.ErrDesc = GetJSONElement(retData, "Errordesc", 0);
        return loginwrapper;
    }

    // Function to Get Tables
    public List<SW_Table> GetTables(String AreaId) {

        JSONObject jObject = new JSONObject();
        if (CallService == true) {
            if (AreaId == "0") {
                String strURL = JSONServiceURL.GetAllOpenOrderTables();
                jObject = json.getHttpJson(strURL);
            } else {
                jObject = json.getHttpJson(JSONServiceURL.GetTables(AreaId));
            }
        } else // For Test Purpose
        {
            String jsonString = "{ \"ResFrom\": \"gettables\", \"ResultSet\": [ { \"AreaID\": 1, \"TableID\": 2, \"TableDesc\": \"Table 1\", \"Status\": null, \"Errorcode\": null, \"Errordesc\": null }, { \"AreaID\": 1, \"TableID\": 4, \"TableDesc\": \"Table 1\", \"Status\": null, \"Errorcode\": null, \"Errordesc\": null } ] }";
            try {
                jObject = new JSONObject(jsonString);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        List<SW_Table> tablewrappers = new ArrayList<SW_Table>();
        JSONArray retData = GetJSONArray(jObject, "ResultSet");
        if (retData != null) {
            for (int counter = 0; counter <= retData.length() - 1; counter++) {
                SW_Table tablewrapper = new SW_Table();
                tablewrapper.Status = GetJSONElement(retData, "Status", counter);
                tablewrapper.AreaID = GetJSONElement(retData, "AreaID", counter);
                tablewrapper.TableID = GetJSONElement(retData, "TableID",
                        counter);
                tablewrapper.TableDesc = GetJSONElement(retData, "TableDesc",
                        counter);
                tablewrapper.ErrCode = GetJSONElement(retData, "Errorcode",
                        counter);
                tablewrapper.ErrDesc = GetJSONElement(retData, "Errordesc",
                        counter);
                tablewrappers.add(tablewrapper);
            }
        }
        return tablewrappers;
    }

    // Function to Get Order Details
    public List<SW_OrderDetails> GetOrderDetails(String Mode, String TableName) {

        JSONObject jObject = new JSONObject();
        if (CallService == true) {
            jObject = json.getHttpJson(JSONServiceURL.GetOrderDetails(Mode,
                    TableName));
        } else // For Test Purpose
        {
            String jsonString = "{ \"ResFrom\": \"getorderdetails\", \"ResultSet\": [ { \"OrderID\": 7, \"OrderDevice\": \"Reception\", \"OrderIP\": \"\", \"OrderTable\": \"Table 1_1\", \"OrderDate\": \"\", \"OrderStatus\": null, \"SendPing\": \"\", \"PaymentMode\": \"\", \"CreditNumber\": \"\", \"DebitNumber\": \"\", \"CancellationReason\": \"\", \"OrderNo\": 0, \"OrderAmount\": 50.0, \"CustId\": 0, \"OrderItems\": [ { \"OrderID\": 7, \"ItemID\": 405, \"Quantity\": 2.0, \"Remarks\": \"Cold\", \"Price\": 15.0, \"Discount\": 0.0, \"SaleTax\": 0.0, \"ItemName\": \"Coffee\", \"ItemTypeId\": 47 } ], \"Status\": \"S1\", \"Errorcode\": null, \"Errordesc\": null } ] }";
            try {
                jObject = new JSONObject(jsonString);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        List<SW_OrderDetails> orderwrappers = new ArrayList<SW_OrderDetails>();
        JSONArray retData = GetJSONArray(jObject, "ResultSet");
        if (retData != null) {
            for (int counter = 0; counter <= retData.length() - 1; counter++) {
                SW_OrderDetails orderwrapper = new SW_OrderDetails();
                orderwrapper.Status = GetJSONElement(retData, "Status", counter);
                orderwrapper.OrderID = GetJSONElement(retData, "OrderID",
                        counter);
                orderwrapper.OrderTable = GetJSONElement(retData, "OrderTable",
                        counter);
                orderwrapper.OrderStatus = GetJSONElement(retData,
                        "OrderStatus", counter);
                orderwrapper.OrderAmount = GetJSONElement(retData,
                        "OrderAmount", counter);
                orderwrapper.ItemTypeId = GetJSONElement(retData, "ItemTypeId",
                        counter);
                JSONArray retItemData = GetJSONArray(jObject, "OrderItems");
                for (int itemcounter = 0; itemcounter <= retItemData.length() - 1; itemcounter++) {
                    SW_ItemDetails itemwrapper = new SW_ItemDetails();
                    itemwrapper.ItemID = GetJSONElement(retData, "ItemID",
                            counter);
                    itemwrapper.ItemName = GetJSONElement(retData, "ItemName",
                            counter);
                    itemwrapper.Quantity = GetJSONElement(retData, "Quantity",
                            counter);
                    itemwrapper.Remarks = GetJSONElement(retData, "Remarks",
                            counter);
                    orderwrapper.ItemDetails.add(itemwrapper);
                }

                orderwrapper.ErrCode = GetJSONElement(retData, "Errorcode",
                        counter);
                orderwrapper.ErrDesc = GetJSONElement(retData, "Errordesc",
                        counter);
                orderwrappers.add(orderwrapper);
            }
        }
        return orderwrappers;
    }

    // Function to Get Order Details
    public List<SW_ItemDetails> GetTableOrderDetails(String Mode, String TableId) {

        JSONObject jObject = new JSONObject();
        if (CallService == true) {
            jObject = json.getHttpJson(JSONServiceURL.GetTableOrderDetails(
                    Mode, TableId));
        } else // For Test Purpose
        {
            String jsonString = "{ \"ResFrom\": \"gettableorder\", \"ResultSet\": [ { \"OrderID\": 3, \"ItemID\": 403, \"Quantity\": 1, \"Remarks\": \"\", \"Price\": 35.0, \"Discount\": 0.0, \"SaleTax\": 0.0, \"ItemName\": \"Hot Chocolate\", \"ItemTypeId\": 47, \"Status\": null, \"Errorcode\": null, \"Errordesc\": null }, { \"OrderID\": 3, \"ItemID\": 405, \"Quantity\": 2, \"Remarks\": \"Cold\", \"Price\": 15.0, \"Discount\": 0.0, \"SaleTax\": 0.0, \"ItemName\": \"Coffee\", \"ItemTypeId\": 47, \"Status\": null, \"Errorcode\": null, \"Errordesc\": null } ] }";
            try {
                jObject = new JSONObject(jsonString);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        List<SW_ItemDetails> itemDetails = new ArrayList<SW_ItemDetails>();
        JSONArray retData = GetJSONArray(jObject, "ResultSet");
        if (retData != null) {
            SW_ItemDetails itemwrappertemp = new SW_ItemDetails();
            itemwrappertemp.Status = GetJSONElement(retData, "Status", 0);
            if (itemwrappertemp.Status.toString().trim().equals("0")) {
                return null;
            }
            for (int counter = 0; counter <= retData.length() - 1; counter++) {
                SW_ItemDetails itemwrapper = new SW_ItemDetails();
                itemwrapper.ItemID = GetJSONElement(retData, "ItemID", counter);
                itemwrapper.ItemName = GetJSONElement(retData, "ItemName",
                        counter);
                itemwrapper.Quantity = GetJSONElement(retData, "Quantity",
                        counter);
                itemwrapper.Remarks = GetJSONElement(retData, "Remarks",
                        counter);
                itemwrapper.ItemTypeId = GetJSONElement(retData, "ItemTypeId",
                        counter);
                itemwrapper.Status = GetJSONElement(retData, "Status", counter);
                itemwrapper.ErrCode = GetJSONElement(retData, "Errorcode",
                        counter);
                itemwrapper.ErrDesc = GetJSONElement(retData, "Errordesc",
                        counter);
                itemDetails.add(itemwrapper);
            }
        }
        return itemDetails;
    }

    public void SaveOrder(String TableId, List<JSONObject> jsonarray) {
        try {

            Map<String, String> kvPairs = new HashMap<String, String>();
            kvPairs.put("orderdetails", jsonarray.toString());
            // Normally I would pass two more JSONObjects.....
            if (CallService == true) {
                HttpResponse re = json.doPost(JSONServiceURL
                        .SaveOrderDetails(TableId.toString().trim()), kvPairs);
                String temp = EntityUtils.toString(re.getEntity());
                if (temp.compareTo("SUCCESS") == 0) {
                    // Toast.makeText(this, "Sending complete!",
                    // Toast.LENGTH_LONG).show();
                }
            } else // For Test Purpose
            {

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Function to Get Areas
    public List<SW_Area> GetAreas() {

        JSONObject jObject = new JSONObject();
        if (CallService == true) {
                jObject = json.getHttpJson(JSONServiceURL.SyscArea());
        } else // For Test Purpose
        {
            String jsonString = "{ \"ResFrom\": \"syncarea\", \"ResultSet\": [ { \"AreaId\": 1, \"AreaDesc\": \"Area 1\", \"Status\": null, \"Errorcode\": null, \"Errordesc\": null }, { \"AreaId\": 2, \"AreaDesc\": \"Area 2\", \"Status\": null, \"Errorcode\": null, \"Errordesc\": null }, { \"AreaId\": 3, \"AreaDesc\": \"Area 3\", \"Status\": null, \"Errorcode\": null, \"Errordesc\": null } ] }";
            try {
                jObject = new JSONObject(jsonString);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        List<SW_Area> areawrappers = new ArrayList<SW_Area>();
        JSONArray retData = GetJSONArray(jObject, "ResultSet");
        if (retData != null) {
            for (int counter = 0; counter <= retData.length() - 1; counter++) {
                SW_Area areawrapper = new SW_Area();
                areawrapper.Status = GetJSONElement(retData, "Status", counter);
                areawrapper.AreaID = GetJSONElement(retData, "AreaId", counter);
                areawrapper.AreaDesc = GetJSONElement(retData, "AreaDesc",
                        counter);
                areawrapper.ErrCode = GetJSONElement(retData, "Errorcode",
                        counter);
                areawrapper.ErrDesc = GetJSONElement(retData, "Errordesc",
                        counter);
                areawrappers.add(areawrapper);
            }
        }
        return areawrappers;
    }
    // Function to Get Table
    public List<SW_Table> GetTables() {

        JSONObject jObject = new JSONObject();
        if (CallService == true) {
                jObject = json.getHttpJson(JSONServiceURL.SyncTable());
        } else // For Test Purpose
        {
            String jsonString = "{ \"ResFrom\": \"synctable\", \"ResultSet\": [ { \"AreaId\": 1, \"TableId\":1.1 , \"TableDesc\": null, \"Errorcode\": null, \"Errordesc\": null }, { \"AreaId\": 2, \"TableId\":2.1\": \"TableDesc\":null,\"Status\": null, \"Errorcode\": null, \"Errordesc\": null }, { \"AreaId\": 3, \"TableId\":2.1\"TableDesc\": 3\", \"Status\": null, \"Errorcode\": null, \"Errordesc\": null } ] }";
            try {
                jObject = new JSONObject(jsonString);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        List<SW_Table> areawrappers = new ArrayList<SW_Table>();
        JSONArray retData = GetJSONArray(jObject, "ResultSet");
        if (retData != null) {
            for (int counter = 0; counter <= retData.length() - 1; counter++) {
                SW_Table areawrapper = new SW_Table();
                areawrapper.Status = GetJSONElement(retData, "Status", counter);
                areawrapper.AreaID = GetJSONElement(retData, "AreaId", counter);
                areawrapper.TableID = GetJSONElement(retData, "TableID",
                        counter);
                areawrapper.TableDesc = GetJSONElement(retData, "TableDesc",
                        counter);
                areawrapper.ErrCode = GetJSONElement(retData, "Errorcode",
                        counter);
                areawrapper.ErrDesc = GetJSONElement(retData, "Errordesc",
                        counter);
                areawrappers.add(areawrapper);
            }
        }
        return areawrappers;
    }
    // Function to Get ItemType
    public List<SW_ItemType> GetItemTypes() {

        JSONObject jObject = new JSONObject();
        if (CallService == true) {
                jObject = json.getHttpJson(JSONServiceURL.SyncItemType());
        } else // For Test Purpose
        {
            String jsonString = "{ \"ResFrom\": \"syncitemtype\", \"ResultSet\": [ {\"Status\":null, \"ItemTypeId\": 1, \"ItemTypeName\":Cold Drinks \"ItemTypeDesc\": null, \"ItemTypeCode\": null, \"Discount\":30,\"SalesTax\":12.50,\"Flag\":null,\"ImageIndex\":null,\"Errorcode\": null, \"Errordesc\": null },{\"Status\":null,\"ItemTypeId\": 2, \"ItemTypeName\":MainCourse,\"ItemTypeDesc\": null, \"ItemTypeCode\": null, \"Discount\":25,\"SalesTax\":12.50,\"Flag\":null,\"ImageIndex\":null,\"Errorcode\": null, \"Errordesc\": null } ] }";
            try {
                jObject = new JSONObject(jsonString);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        List<SW_ItemType> areawrappers = new ArrayList<SW_ItemType>();
        JSONArray retData = GetJSONArray(jObject, "ResultSet");
        if (retData != null) {
            for (int counter = 0; counter <= retData.length() - 1; counter++) {
                SW_ItemType areawrapper  = new SW_ItemType();
                areawrapper.Status       = GetJSONElement(retData, "Status", counter);
                areawrapper.ItemTypeId   = GetJSONElement(retData, "ItemTypeId", counter);
                areawrapper.ItemTypeName = GetJSONElement(retData, "ItemTypeName", counter);
                areawrapper.ItemTypeDesc = GetJSONElement(retData, "ItemTypeDesc", counter);
                areawrapper.ItemTypeCode = GetJSONElement(retData, "ItemTypeCode", counter);
                areawrapper.Discount     = GetJSONElement(retData, "Discount",counter);
                areawrapper.SalesTax     = GetJSONElement(retData, "SalesTax",counter);
                areawrapper.Flag         = GetJSONElement(retData, "Flag",counter);
                areawrapper.ImageIndex   = GetJSONElement(retData, "ImageIndex",counter);
                areawrapper.ErrCode      = GetJSONElement(retData, "ErrCode",counter);
                areawrapper.ErrDesc      = GetJSONElement(retData, "ErrDesc",counter);      

                areawrappers.add(areawrapper);
            }
        }
        return areawrappers;
    }
    // Function to Get Item
    public List<SW_Item> GetItems() {

        JSONObject jObject = new JSONObject();
        if (CallService == true) {
                jObject = json.getHttpJson(JSONServiceURL.SyscArea());
        } else // For Test Purpose
        {
            String jsonString = "{ \"ResFrom\": \"syncitem\", \"ResultSet\": [ { \"Status\": 1, \"ItemId\":1, \"ItemTypeId\":2,\"ItemName\":Jaljira,\"ItemDesc\":null,\"Price\":20,\"Active\":null,\"ItemCode\":null, \"Errorcode\": null, \"Errordesc\": null },{ \"Status\": 1, \"ItemId\":2, \"ItemTypeId\":3,\"ItemName\":NimbuPani,\"ItemDesc\":null,\"Price\":15,\"Active\":null,\"ItemCode\":null, \"Errorcode\": null, \"Errordesc\": null } ] }";
            try {
                jObject = new JSONObject(jsonString);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        List<SW_Item> areawrappers = new ArrayList<SW_Item>();
        JSONArray retData = GetJSONArray(jObject, "ResultSet");
        if (retData != null) {
            for (int counter = 0; counter <= retData.length() - 1; counter++) {
                SW_Item areawrapper      = new SW_Item();
                areawrapper.Status       = GetJSONElement(retData, "Status", counter);
                areawrapper.ItemId       = GetJSONElement(retData, "ItemId", counter);
                areawrapper.ItemTypeId   = GetJSONElement(retData, "ItemTypeId",counter);
                areawrapper.ItemName     = GetJSONElement(retData, "ItemName", counter);
                areawrapper.ItemDesc     = GetJSONElement(retData, "ItemDesc", counter);
                areawrapper.Price        = GetJSONElement(retData, "Price", counter);
                areawrapper.Active       = GetJSONElement(retData, "Active", counter);
                areawrapper.ItemCode     = GetJSONElement(retData, "ItemCode", counter);                
                areawrapper.ErrCode      = GetJSONElement(retData, "Errorcode",counter);
                areawrapper.ErrDesc      = GetJSONElement(retData, "Errordesc",counter);

                areawrappers.add(areawrapper);
            }
        }
        return areawrappers;
    }






















    private String Encrypt(String password) {
        String toEnc = password; // Value to encrypt
        MessageDigest mdEnc = null;
        try {
            mdEnc = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        // Encryption algorithm
        mdEnc.update(toEnc.getBytes(), 0, toEnc.length());
        String md5 = new BigInteger(1, mdEnc.digest()).toString(16);
        if (md5.length() < 32)
            md5 = "0" + md5;
        return md5;
    }

    /* **************************************** */
    /* Private Methods To Extract JSON Contents */
    /* **************************************** */

    private JSONArray GetJSONArray(JSONObject obj, String arrayName) {
        JSONArray retData = null;
        try {
            retData = obj.getJSONArray(arrayName);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return retData;
    }

    private String GetJSONElement(JSONArray jsonArr, String Element, int index) {
        String element = "";
        try {
            element = jsonArr.getJSONObject(index).getString(Element)
                    .toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return element;
    }

    private String GetJSONString(JSONObject jObj, String Element) {
        String element = "";
        try {
            element = jObj.getString(Element).toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return element;
    }

    private JSONObject GetJSONObject(JSONArray jsonArr, int index) {
        JSONObject retData = null;
        try {
            retData = jsonArr.getJSONObject(index);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return retData;
    }
}
