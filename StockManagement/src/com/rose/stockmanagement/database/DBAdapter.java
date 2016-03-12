package com.rose.stockmanagement.database;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.codegineer.datagrid.DataGrid;
import com.codegineer.datatable.DataTable.DataRow;
import com.rose.stockmanagement.R;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

	public class DBAdapter {
	
		private static final String DATABASE_NAME = "FSSNLDB";
		private static final int DATABASE_VERSION = 1;
		
		// Create Category Table
		private static final String DATABASE_CATEGORY_CREATE = "CREATE TABLE if not exists "
				+ "Category"
				+ " ("
				+ "ID"
				+ " TEXT PRIMARY KEY,"
				+ "NAME NOT NULL"
				+ ","
				+ "SORTORDER NOT NULL"
				+ ","
				+ "DEFAULTFLAG NOT NULL"
				+ ","
				+ "SYMBOL NOT NULL"
				+ ","
				+ "USEFLAG NOT NULL" + ");";
		
		// Create Company Table
		private static final String DATABASE_COMPANY_CREATE = "CREATE TABLE if not exists "
				+ "Company ("
				+ "ID TEXT PRIMARY KEY,"
				+ "NAME,"
				+ "BIZSERIAL,"
				+ "REPRESENTATIVE,"
				+ "PHONE,"
				+ "FAX,"
				+ "ADDRESS1,"
				+ "ADDRESS2,"
				+ "TYPEID,"
				+ "PERIODDAY);";
						
		// Create Product Table
		private static final String DATABASE_PRODUCT_CREATE = "CREATE TABLE if not exists "
				+ "Product ("
				+ "ID TEXT,"
				+ "MAKERCODE TEXT,"
				+ "NAME NOT NULL,"
				+ "CATEGORYID NOT NULL,"
				+ "PRICE,"
				+ "ORGPRICE,"
				+ "MODEL,"
				+ "SPECSIZEID,"
				+ "TEAMID,"
				+ "BRANDID,"
				+ "COLOR,"
				+ "MATERIAL,"
				+ "LOGISTICSCODEA1 TEXT,"
				+ "LOGISTICSCODEA2 TEXT,"
				+ "LOGISTICSUNITA,"
				+ "LOGISTICSCODEB1 TEXT,"
				+ "LOGISTICSCODEB2 TEXT,"
				+ "LOGISTICSUNITB,"
				+ "LOGISTICSCODEC1 TEXT,"
				+ "LOGISTICSCODEC2 TEXT,"
				+ "LOGISTICSUNITC,"
				+ "MEMO,"
				+ "MANUFACTURERID,"
				+ "PRIMARY KEY(ID, MAKERCODE, LOGISTICSCODEA1, LOGISTICSCODEA2, LOGISTICSCODEB1, LOGISTICSCODEB2, LOGISTICSCODEC1, LOGISTICSCODEC2));";
		
		// Crate ProductCompanyStockOut
		private static final String DATABASE_PRODUCTCOMPANYSTOCKOUT_CREATE = "CREATE TABLE if not exists "
				+ "ProductCompanyStockOut ("
				+ "ID TEXT PRIMARY KEY,"				
				+ "COMPANYID,"
				+ "OUTDATEFROM,"
				+ "OUTDATETO,"
				+ "WAREHOUSEID,"
				+ "TRANTYPEID,"
				+ "TOTALQTY,"
				+ "TOTALAMT,"
				+ "ALLTOTALAMT,"
				+ "TAXINFLAG,"
				+ "MEMO,"
				+ "UPDATESTAFF,"				
				+ "OUTDATE,"
				+ "DCAMT,"
				+ "RESULTAMT,"
				+ "ORDERID,"
				+ "TOTALDCAMT,"
				+ "TOTALRESULTAMT,"				
				+ "STEPID);";
		
		// Create ProductCompanyStockOutItem
		private static final String DATABASE_PRODUCTCOMPANYSTOCKOUTITEM_CREATE = "CREATE TABLE if not exists "
				+ "ProductCompanyStockOutItem ("
				+ "ID TEXT PRIMARY KEY,"				
				+ "PRODUCTID,"
				+ "PRICE,"
				+ "ORGPRICE,"
				+ "QTY,"
				+ "AMT,"
				+ "TAXAMT,"
				+ "TOTALAMT,"
				+ "TAXINFLAG,"				
				+ "MEMO,"
				+ "PRODUCTNAME,"								
				+ "DCAMT,"
				+ "RESULTAMT);";
		
		// Create ProductPO Table
		private static final String DATABASE_PRODUCTPO_CREATE = "CREATE TABLE if not exists "
				+ "ProductPO ("
				+ "ID TEXT PRIMARY KEY,"				
				+ "WAREHOUSEID,"
				+ "ORDERDATE,"
				+ "WILLSTOCKINDATE,"
				+ "TOTALQTY,"
				+ "TOTALAMT,"
				+ "TOTALTAXAMT,"
				+ "ALLTOTALAMT,"
				+ "MEMO,"				
				+ "UPDATESTAFF,"
				+ "TAXINFLAG,"								
				+ "STEPID,"
				+ "MANUFACTURERID,"
				+ "UPDATEDATE,"
				+ "INSERTDATE);";
		
		// Create ProductPOItem Table
		private static final String DATABASE_PRODUCTPOITEM_CREATE = "CREATE TABLE if not exists "
				+ "ProductPOItem ("
				+ "ID TEXT,"				
				+ "PRODUCTID TEXT,"
				+ "ORGPRICE,"
				+ "QTY,"
				+ "AMT,"
				+ "TAXAMT,"
				+ "TOTALAMT,"
				+ "MEMO,"
				+ "PRIMARY KEY(ID, PRODUCTID));";
		
		// Create ProductPOItemTemp Table
		private static final String DATABASE_PRODUCTPOITEMTEMP_CREATE = "CREATE TABLE if not exists "
				+ "ProductPOItemTemp ("
				+ "ID TEXT,"				
				+ "PRODUCTID TEXT,"
				+ "ORGPRICE,"
				+ "QTY,"
				+ "AMT,"
				+ "TAXAMT,"
				+ "TOTALAMT,"
				+ "MEMO,"
				+ "INQTY,"
				+ "INPUTQTY,"
				+ "PRIMARY KEY(ID, PRODUCTID));";
		
		// Create ProductPOTemp Table
		private static final String DATABASE_PRODUCTPOTEMP_CREATE = "CREATE TABLE if not exists "
				+ "ProductPOTemp ("
				+ "ID TEXT PRIMARY KEY,"				
				+ "WAREHOUSEID,"
				+ "ORDERDATE,"
				+ "WILLSTOCKINDATE,"
				+ "TOTALQTY,"
				+ "TOTALAMT,"
				+ "TOTALTAXAMT,"
				+ "ALLTOTALAMT,"
				+ "MEMO,"				
				+ "UPDATESTAFF,"
				+ "TAXINFLAG,"								
				+ "STEPID,"
				+ "MANUFACTURERID,"
				+ "UPDATEDATE,"
				+ "INSERTDATE,"
				+ "TOTALINQTY,"
				+ "TOTALINPUTQTY);";
		
		// Create ProductSOCompany Table
		private static final String DATABASE_PRODUCTSOCOMPANY_CREATE = "CREATE TABLE if not exists "
				+ "ProductSOCompany ("
				+ "ID TEXT PRIMARY KEY,"				
				+ "STOREID,"
				+ "ORDERDATE,"
				+ "STEPID,"
				+ "WAREHOUSEID,"
				+ "WILLSTOCKOUTDATE,"
				+ "TRANTYPEID,"
				+ "TOTALQTY,"
				+ "TOTALAMT,"				
				+ "TOTALTAXAMT,"
				+ "ALLTOTALAMT,"								
				+ "TAXINFLAG,"
				+ "MEMO,"
				+ "UPDATESTAFF,"
				+ "COMPANYID,"
				+ "TOTALDCAMT,"
				+ "TOTALRESULTAMT,"
				+ "TOTALOUTQTY,"
				+ "TOTALINPUTQTY);";
		
		// Create ProductSOCompanyItem Table
		private static final String DATABASE_PRODUCTSOCOMPANYITEM_CREATE = "CREATE TABLE if not exists "
				+ "ProductSOCompanyItem ("
				+ "ID TEXT,"				
				+ "PRODUCTID TEXT,"
				+ "TRANTYPEID,"
				+ "PRICE,"
				+ "QTY,"
				+ "AMT,"
				+ "TAXAMT,"
				+ "TOTALAMT,"
				+ "MEMO,"				
				+ "DCAMT,"
				+ "RESULTAMT,"								
				+ "OUTQTY,"
				+ "INPUTQTY,"
				+ "PRIMARY KEY(ID, PRODUCTID));";
		
		// Create ProductSOStore Table
		private static final String DATABASE_PRODUCTSOSTORE_CREATE = "CREATE TABLE if not exists "
				+ "ProductSOStore ("
				+ "ID TEXT PRIMARY KEY,"								
				+ "STOREID,"
				+ "ORDERDATE,"
				+ "STEPID,"
				+ "WAREHOUSEID,"
				+ "WILLSTOCKOUTDATE,"
				+ "TRANTYPEID,"
				+ "TOTALQTY,"				
				+ "TOTALAMT,"
				+ "TOTALTAXAMT,"								
				+ "ALLTOTALAMT,"
				+ "TAXINFLAG,"
				+ "MEMO,"
				+ "UPDATESTAFF,"
				+ "TOTALDCAMT,"
				+ "TOTALRESULTAMT,"
				+ "STOREWAREHOUSEID,"
				+ "TOTALOUTQTY,"
				+ "TOTALINPUTQTY);";		
		
		// Create ProductSOStoreItem Table
		private static final String DATABASE_PRODUCTSOSTOREITEM_CREATE = "CREATE TABLE if not exists "
				+ "ProductSOStoreItem ("
				+ "ID TEXT,"								
				+ "PRODUCTID TEXT,"
				+ "TRANTYPEID,"
				+ "PRICE,"
				+ "QTY,"
				+ "AMT,"
				+ "TAXAMT,"
				+ "TOTALAMT,"				
				+ "MEMO,"
				+ "DCAMT,"								
				+ "RESULTAMT,"
				+ "OUTQTY,"
				+ "INPUTQTY,"
				+ "PRIMARY KEY(ID, PRODUCTID));";	
		
		// Create ProductStock Table
		private static final String DATABASE_PRODUCTSTOCK_CREATE = "CREATE TABLE if not exists "
				+ "ProductStock ("								
				+ "PRODUCTID TEXT PRIMARY KEY,"
				+ "WAREHOUSEORSTOREID,"
				+ "CATEGORYID,"
				+ "PRODUCTNAME,"
				+ "STOCKQUANTITY);";	
		
		// Create ProductStockChecking Table
		private static final String DATABASE_PRODUCTSTOCKCHECKING_CREATE = "CREATE TABLE if not exists "
				+ "ProductStockChecking ("								
				+ "ID TEXT PRIMARY KEY,"
				+ "WAREHOUSEID,"
				+ "TOTALPRODUCTQTY,"
				+ "TOTALQTY,"
				+ "MEMO,"
				+ "INSERTDATE,"
				+ "INSERTSTAFF,"
				+ "WORKDATE);";
		
		// Create ProductStockCheckingItem Table
		private static final String DATABASE_PRODUCTSTOCKCHECKINGITEM_CREATE = "CREATE TABLE if not exists "
				+ "ProductStockCheckingItem ("								
				+ "ID TEXT,"
				+ "WAREHOUSEID,"
				+ "PRODUCTID TEXT,"
				+ "QTY,"
				+ "WORKDATE,"
				+ "PRODUCTNAME,"
				+ "WORKERID,"
				+ "PRIMARY KEY(ID, PRODUCTID));";
		
		// Create ProductStockIn Table
		private static final String DATABASE_PRODUCTSTOCKIN_CREATE = "CREATE TABLE if not exists "
				+ "ProductStockIn ("								
				+ "ID TEXT PRIMARY KEY,"
				+ "INDATEFROM,"
				+ "INDATETO,"
				+ "ORDERID,"
				+ "WAREHOUSEID,"
				+ "INDATE,"
				+ "TRANTYPEID,"
				+ "TOTALQTY,"
				+ "TOTALAMT,"
				+ "TOTALTAXAMT,"
				+ "ALLTOTALAMT,"
				+ "TAXINFLAG,"
				+ "MEMO,"
				+ "UPDATESTAFF,"
				+ "MANUFACTURERID,"
				+ "STAFFID);";
		
		// Create ProductStockItem Table
		private static final String DATABASE_PRODUCTSTOCKITEM_CREATE = "CREATE TABLE if not exists "
				+ "ProductStockItem ("								
				+ "ID TEXT,"
				+ "PRODUCTID TEXT,"
				+ "PRICE,"
				+ "ORGPRICE,"
				+ "QTY,"
				+ "AMT,"
				+ "TAXAMT,"
				+ "TOTALAMT,"
				+ "TAXINFLAG,"
				+ "MEMO,"
				+ "PRIMARY KEY(ID, PRODUCTID));";
		
		// Create ProductStoreStockOut Table
		private static final String DATABASE_PRODUCTSTORESTOCKOUT_CREATE = "CREATE TABLE if not exists "
				+ "ProductStoreStockOut ("								
				+ "ID TEXT PRIMARY KEY,"
				+ "OUTDATEFROM,"
				+ "OUTDATETO,"
				+ "WAREHOUSEID,"
				+ "STOREID,"
				+ "ORDERID,"
				+ "TRANTYPEID,"
				+ "TOTALQTY,"
				+ "TOTALAMT,"
				+ "TOTALTAXAMT,"
				+ "ALLTOTALAMT,"
				+ "TAXINFLAG,"
				+ "MEMO,"
				+ "UPDATESTAFF,"
				+ "OUTDATE,"
				+ "DCAMT,"
				+ "RESULTAMT,"
				+ "TOTALDCAMT,"
				+ "TOTALRESULTAMT,"
				+ "STEPID,"
				+ "STOREWAREHOUSEID);";
		
		// Create ProductStoreStockOutItem Table
		private static final String DATABASE_PRODUCTSTORESTOCKOUTITEM_CREATE = "CREATE TABLE if not exists "
				+ "ProductStoreStockOutItem ("								
				+ "ID TEXT,"
				+ "PRODUCTID TEXT,"
				+ "PRICE,"
				+ "ORGPRICE,"
				+ "QTY,"
				+ "AMT,"
				+ "TAXAMT,"
				+ "TOTALAMT,"
				+ "TAXINFLAG,"
				+ "MEMO,"
				+ "DCAMT,"
				+ "RESULTAMT,"
				+ "PRIMARY KEY(ID, PRODUCTID));";
		
		// Create ProductWarehouseTransfer Table
		private static final String DATABASE_PRODUCTWAREHOUSETRANSFER_CREATE = "CREATE TABLE if not exists "
				+ "ProductWarehouseTransfer ("								
				+ "ID TEXT PRIMARY KEY,"
				+ "FROMWAREHOUSEID,"
				+ "TOWAREHOUSEID,"
				+ "WORKDATE,"
				+ "TOTALPRODUCTQTY,"
				+ "MEMO,"
				+ "STAFFID,"
				+ "TOTALQTY);";
		
		// Create ProductWarehouseTransferItem Table
		private static final String DATABASE_PRODUCTWAREHOUSETRANSFERITEM_CREATE = "CREATE TABLE if not exists "
				+ "ProductWarehouseTransferItem ("								
				+ "ID TEXT PRIMARY KEY,"
				+ "PRODUCTID,"
				+ "QTY,"
				+ "WORKDATE,"
				+ "WORKERID);";
		
		// Create Staff Table
		private static final String DATABASE_STAFF_CREATE = "CREATE TABLE if not exists "
				+ "Staff ("								
				+ "ID TEXT PRIMARY KEY,"
				+ "USERID,"
				+ "PASSWD,"
				+ "NAME,"
				+ "DBDATETIME);";
		
		// Create Test Table
		private static final String DATABASE_TEST_CREATE = "CREATE TABLE if not exists "
				+ "Test ("								
				+ "ID INTEGER,"
				+ "BARCODE,"
				+ "TITLE);";
		
		// Create Version Table
		private static final String DATABASE_VERSION_CREATE = "CREATE TABLE if not exists "
				+ "Version ("
				+ "APPCODE,"
				+ "VERSIONID,"
				+ "PRODUCTITSERIAL,"
				+ "FILENAME,"
				+ "FILEFLAG);";
		
		
		private DatabaseHelper mDbHelper;
		private SQLiteDatabase mDb;
		private final Context mCtx;
		
		private static class DatabaseHelper extends SQLiteOpenHelper {
			DatabaseHelper(Context context) {
				super(context, DATABASE_NAME, null, DATABASE_VERSION);
			}

			@Override
			public void onCreate(SQLiteDatabase db) {
				// TODO Auto-generated method stub
				db.execSQL(DATABASE_CATEGORY_CREATE);
				db.execSQL(DATABASE_COMPANY_CREATE);
				db.execSQL(DATABASE_PRODUCT_CREATE);
				db.execSQL(DATABASE_PRODUCTCOMPANYSTOCKOUT_CREATE);
				db.execSQL(DATABASE_PRODUCTCOMPANYSTOCKOUTITEM_CREATE);
				db.execSQL(DATABASE_PRODUCTPO_CREATE);
				db.execSQL(DATABASE_PRODUCTPOITEM_CREATE);
				db.execSQL(DATABASE_PRODUCTPOITEMTEMP_CREATE);
				db.execSQL(DATABASE_PRODUCTPOTEMP_CREATE);
				db.execSQL(DATABASE_PRODUCTSOCOMPANY_CREATE);
				db.execSQL(DATABASE_PRODUCTSOSTORE_CREATE);
				db.execSQL(DATABASE_PRODUCTSOSTOREITEM_CREATE);
				db.execSQL(DATABASE_PRODUCTSTOCK_CREATE);
				db.execSQL(DATABASE_PRODUCTSTOCKCHECKING_CREATE);
				db.execSQL(DATABASE_PRODUCTSTOCKCHECKINGITEM_CREATE);
				db.execSQL(DATABASE_PRODUCTSTOCKIN_CREATE);
				db.execSQL(DATABASE_PRODUCTSTOCKITEM_CREATE);
				db.execSQL(DATABASE_PRODUCTSTORESTOCKOUT_CREATE);
				db.execSQL(DATABASE_PRODUCTSTORESTOCKOUTITEM_CREATE);
				db.execSQL(DATABASE_PRODUCTWAREHOUSETRANSFER_CREATE);
				db.execSQL(DATABASE_PRODUCTWAREHOUSETRANSFERITEM_CREATE);
				db.execSQL(DATABASE_STAFF_CREATE);
				db.execSQL(DATABASE_TEST_CREATE);
				db.execSQL(DATABASE_VERSION_CREATE);
			}

			@Override
			public void onUpgrade(SQLiteDatabase db, int oldVersion,
					int newVersion) {
				// TODO Auto-generated method stub
				onCreate(db);
				
			}
		}
		
		public DBAdapter(Context ctx) {
			this.mCtx = ctx;
		}

		public DBAdapter open() throws SQLException {
			mDbHelper = new DatabaseHelper(mCtx);
			mDb = mDbHelper.getWritableDatabase();
			
			return this;
		}

		public void close() {
			if (mDbHelper != null) {
				mDbHelper.close();
			}
		}
		
		public int GetLoginCount() throws SQLException
		{
			int nCount = 0;
			
			Cursor mCursor = null;
			try
			{
				mCursor = mDb.query("Staff",
						new String[] { "COUNT(USERID) COUNT" },
						null, null, null, null, null);
				
				if (mCursor != null) {
					mCursor.moveToFirst();
				}
				
				if (mCursor != null) {
					try {
						do {
							nCount = mCursor
									.getInt(mCursor
											.getColumnIndex("COUNT"));
						} while (mCursor.moveToNext());
					} catch (Exception e) {
						// TODO: handle exception
					}
				}
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
			
			return nCount;
		}
		
		public String GetStoreName(String strID) throws SQLException
		{
			String strStoreName = "";
			
			Cursor mCursor = null;
			try
			{
				String strWhere = "ID = '" + strID + "'";
				mCursor = mDb.query("Category",
						new String[] { "NAME" },
						strWhere, null, null, null, null);
				
				if (mCursor != null) {
					mCursor.moveToFirst();
				}
				
				if (mCursor != null) {
					try {
						do {
							strStoreName = mCursor
									.getString(mCursor
											.getColumnIndex("NAME"));
						} while (mCursor.moveToNext());
					} catch (Exception e) {
						// TODO: handle exception
					}
				}
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
			
			return strStoreName;
		}
		
		public String GetWarehouseName(String strID) throws SQLException
		{
			String strWarehouseName = "";
			
			Cursor mCursor = null;
			try
			{
				String strWhere = "ID = '" + strID + "'";
				mCursor = mDb.query("Category",
						new String[] { "NAME" },
						strWhere, null, null, null, null);
				
				if (mCursor != null) {
					mCursor.moveToFirst();
				}
				
				if (mCursor != null) {
					try {
						do {
							strWarehouseName = mCursor
									.getString(mCursor
											.getColumnIndex("NAME"));
						} while (mCursor.moveToNext());
					} catch (Exception e) {
						// TODO: handle exception
					}
				}
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
			finally
			{
				if(mCursor != null)
					mCursor.close();
			}
			
			return strWarehouseName;
		}
		
		public String GetUserName(String strID) throws SQLException
		{
			String strUserName = "";
			
			Cursor mCursor = null;
			try
			{
				String strWhere = "ID = '" + strID + "'";
				mCursor = mDb.query("Staff",
						new String[] { "NAME" },
						strWhere, null, null, null, null);
				
				if (mCursor != null) {
					mCursor.moveToFirst();
				}
				
				if (mCursor != null) {
					try {
						do {
							strUserName = mCursor
									.getString(mCursor
											.getColumnIndex("NAME"));
						} while (mCursor.moveToNext());
					} catch (Exception e) {
						// TODO: handle exception
					}
				}
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
			finally
			{
				if(mCursor != null)
					mCursor.close();
			}
			
			return strUserName;
		}
		
		public String GetStoreOrWarehouseID(String strName) throws SQLException
		{
			String strID = "";
			
			
			try
			{
				strID = GetStoreid(strName);
				if (strID.length() == 0)
                {
					strID = GetWarehouseid(strName);
                }
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
			
			return strID;
		}
		
		public String GetStoreid(String strName) throws SQLException
		{
			String strID = "";
			
			Cursor mCursor = null;
			try
			{
				String strWhere = "(NAME='" + strName + "') AND (TYPEID='CY03' OR TYPEID='CY04' OR TYPEID='CY02')";
				mCursor = mDb.query("COMPANY",
						new String[] { "ID" },
						strWhere, null, null, null, null);
				
				if (mCursor != null) {
					mCursor.moveToFirst();
				}
				
				if (mCursor != null) {
					try {
						strID = mCursor
								.getString(mCursor
										.getColumnIndex("ID"));
					} catch (Exception e) {
						// TODO: handle exception
					}
				}
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
			
			return strID;
		}
		
		public String GetWarehouseid(String strName) throws SQLException
		{
			String strID = "";
			
			Cursor mCursor = null;
			try
			{
				String strWhere = "(NAME='" + strName + "') AND (ID LIKE 'WH%')";
				mCursor = mDb.query("CATEGORY",
						new String[] { "ID" },
						strWhere, null, null, null, null);
				
				if (mCursor != null) {
					mCursor.moveToFirst();
				}
				
				if (mCursor != null) {
					try {
						strID = mCursor
								.getString(mCursor
										.getColumnIndex("ID"));
					} catch (Exception e) {
						// TODO: handle exception
					}
				}
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
			
			return strID;
		}
		
		public String GetCategoryCode(String strName) throws SQLException
		{
			String strID = "";
			
			Cursor mCursor = null;
			try
			{
				String strWhere = "NAME = '" + strName + "' AND ID LIKE 'PT%'";
				mCursor = mDb.query("Category",
						new String[] { "ID" },
						strWhere, null, null, null, null);
				
				if (mCursor != null) {
					mCursor.moveToFirst();
				}
				
				if (mCursor != null) {
					try {
						strID = mCursor
								.getString(mCursor
										.getColumnIndex("ID"));
					} catch (Exception e) {
						// TODO: handle exception
					}
				}
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
			
			return strID;
		}
		
		public Cursor GetStockReplaceItemSum() throws SQLException
		{
			int nCount = 0;
			
			Cursor mCursor = null;
			try
			{
				mCursor = mDb.query("ProductWarehouseTransferItem",
						new String[] { "ID", "COUNT(ID) CNT", "SUM(CAST(QTY AS INTEGER)) AS TOTALQTY" },
						null, null, "ID", null, null);
				
				if (mCursor != null) {
					mCursor.moveToFirst();
				}
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
			
			return mCursor;
		}
		
		public Cursor GetStockReplaceChecking() throws SQLException
		{
			int nCount = 0;
			
			Cursor mCursor = null;
			try
			{
				mCursor = mDb.query("ProductWarehouseTransfer",
						new String[] { "*" },
						null, null, null, null, "ID");
				
				if (mCursor != null) {
					mCursor.moveToFirst();
				}
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
			
			return mCursor;
		}
		
		public Cursor GetStockcheckingItemSum(String id) throws SQLException
		{
			int nCount = 0;
			
			Cursor mCursor = null;
			try
			{
				String strWhere = "";
				if(id.length() > 0)
					strWhere += " ID='" + id + "'";
				
				mCursor = mDb.query("ProductStockCheckingItem",
						new String[] { "ID", "COUNT(ID) CNT", "SUM(CAST(QTY AS INTEGER)) AS TOTALQTY" },
						strWhere, null, "ID", null, null);
				
				if (mCursor != null) {
					mCursor.moveToFirst();
				}
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
			
			return mCursor;
		}
		
		public Cursor GetStockChecking(String id) throws SQLException
		{
			int nCount = 0;
			
			Cursor mCursor = null;
			try
			{
				String strWhere = "";
				if(id.length() > 0)
					strWhere += " ID='" + id + "'";
				
				mCursor = mDb.query("ProductStockChecking",
						new String[] { "*" },
						strWhere, null, null, null, "ID");
				
				if (mCursor != null) {
					mCursor.moveToFirst();
				}
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
			
			return mCursor;
		}
		
		public Cursor GetStockReplace() throws SQLException
		{
			int nCount = 0;
			
			Cursor mCursor = null;
			try
			{
				mCursor = mDb.query("ProductWarehouseTransfer",
						new String[] { "*" },
						null, null, null, null, "WORKDATE");
				
				if (mCursor != null) {
					mCursor.moveToFirst();
				}
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
			
			return mCursor;
		}
		
		public Boolean deleteUploadList(DataRow dr)
		{
			Boolean bRet = true;
			
			String sqlWhere = "";

            sqlWhere = " ID='" + dr.get("col2") + "'";
            
            if(mDb.delete("ProductStockChecking", sqlWhere, null) > 0)
            {
            	mDb.delete("ProductStockCheckingItem", sqlWhere, null);
            }
            else
            	bRet = false;
            
            return bRet;
		}
		
		public Boolean deleteUploadListForReplace(DataRow dr)
		{
			Boolean bRet = true;
			
			String sqlWhere = "";

            sqlWhere = " ID='" + dr.get("col9") + "'";
            
            if(mDb.delete("ProductWarehouseTransfer", sqlWhere, null) > 0)
            {
            	mDb.delete("ProductWarehouseTransferItem", sqlWhere, null);
            }
            else
            	bRet = false;
            
            return bRet;
		}
		
		public Boolean deleteStockChecking(DataGrid dg)
		{
			Boolean bRet = true;
			
			String sqlWhere = "";
			for(int i = 0; i < dg.GetDataGridAdapter().getCount(); i++)
			{
				if(dg.GetDataGridAdapter().getIsSelected().get(i))
				{						
					DataRow dr = dg.GetDataSource().getRow(i);
					if(sqlWhere.length() == 0)
						sqlWhere = " ID IN('" + dr.get("col2") + "'";
					else
	                    sqlWhere += ",'" + dr.get("col2") + "'";
				}
			}
			
			sqlWhere += ")";
			
			if(mDb.delete("ProductStockChecking", sqlWhere, null) > 0)
			{
				mDb.delete("ProductStockCheckingItem", sqlWhere, null);
			}
			else
				bRet = false;
			
			return bRet;
		}
		
		public Boolean deleteStockCheckingForReplace(DataGrid dg)
		{
			Boolean bRet = true;
			
			String sqlWhere = "";
			for(int i = 0; i < dg.GetDataGridAdapter().getCount(); i++)
			{
				if(dg.GetDataGridAdapter().getIsSelected().get(i))
				{						
					DataRow dr = dg.GetDataSource().getRow(i);
					if(sqlWhere.length() == 0)
						sqlWhere = " ID IN('" + dr.get("col9") + "'";
					else
	                    sqlWhere += ",'" + dr.get("col9") + "'";
				}
			}
			
			sqlWhere += ")";
			
			if(mDb.delete("ProductWarehouseTransfer", sqlWhere, null) > 0)
			{
				mDb.delete("ProductWarehouseTransferItem", sqlWhere, null);
			}
			else
				bRet = false;
			
			return bRet;
		}
		
		public Boolean deleteStockCheckingForReplaceItem(DataGrid dg, String strID)
		{
			Boolean bRet = true;
			
			String sqlWhere = "";
			for(int i = 0; i < dg.GetDataGridAdapter().getCount(); i++)
			{
				if(dg.GetDataGridAdapter().getIsSelected().get(i))
				{						
					DataRow dr = dg.GetDataSource().getRow(i);
					if(sqlWhere.length() == 0)
						sqlWhere = " PRODUCTID IN('" + dr.get("col2").toString() + "'";
					else
	                    sqlWhere += ",'" + dr.get("col2").toString() + "'";
				}
			}
			
			sqlWhere += ")";
			sqlWhere += " AND ID = '" + strID + "'";
			
			if(mDb.delete("ProductWarehouseTransferItem", sqlWhere, null) > 0)
				bRet = true;
			
			return bRet;
		}
		
		public Boolean deleteStockCheckingItem(DataGrid dg, String strID)
		{
			Boolean bRet = true;
			
			String sqlWhere = "";
			for(int i = 0; i < dg.GetDataGridAdapter().getCount(); i++)
			{
				if(dg.GetDataGridAdapter().getIsSelected().get(i))
				{						
					DataRow dr = dg.GetDataSource().getRow(i);
					if(sqlWhere.length() == 0)
						sqlWhere = " PRODUCTID IN('" + dr.get("col2").toString() + "'";
					else
	                    sqlWhere += ",'" + dr.get("col2").toString() + "'";
				}
			}
			
			sqlWhere += ")";
			sqlWhere += " AND ID = '" + strID + "'";
			
			if(mDb.delete("ProductStockCheckingItem", sqlWhere, null) > 0)
				bRet = true;
			
			return bRet;
		}
		
		public Cursor GetUserInfo() throws SQLException
		{
			Cursor mCursor = null;
			try {
				mCursor = mDb.query("Staff",
						new String[] { "ID",
								"USERID",
								"PASSWD",
								"DBDATETIME",
								"NAME"},
						null, null, null, null, null);
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}

			return mCursor;
		}
		
		public Cursor GetStockReplaceItem(String strId) throws SQLException
		{
			Cursor mCursor = null;
			try {
				
				String strWhere = "ID = '" + strId + "'";
				mCursor = mDb.query("ProductWarehouseTransferItem",
						new String[] { "*" },
						strWhere, null, null, null, null);
				
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}

			return mCursor;
		}
		
		public Cursor GetWarehouseList(String id) throws SQLException
		{
			Cursor mCursor = null;
			try {
				String strWhere = "";
				
				if (id.length() > 0)
                {
					strWhere += "(ID LIKE 'WH%') AND (LENGTH(ID) = 6) AND (SUBSTR(ID,6,1)='2') AND USEFLAG = 'Y' ";
					strWhere += "ORDER BY ID";
                }
                else
                {
                	strWhere += "(ID LIKE 'WH%') AND (LENGTH(ID) = 6) AND (SUBSTR(ID,6,1)='1' OR SUBSTR(ID,6,1)='3') AND USEFLAG = 'Y' ";
                	strWhere += "ORDER BY ID";
                }
				
				mCursor = mDb.query("Category",
						new String[] { "*" },
						strWhere, null, null, null, null);
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}

			return mCursor;
		}
		
		public Cursor GetCategoryList(String type, String code) throws SQLException
		{
			Cursor mCursor = null;
			try {
				String strWhere = "";
				
				if (type.equals("L"))
                {
					strWhere += " (ID LIKE 'PT%') AND (LENGTH(ID) = 4)";
                }
                else if (type.equals("M"))
                {
                	strWhere += " (ID LIKE '" + code + "%') AND (LENGTH(ID) = 6)";
                }
                else if (type.equals("S"))
                {
                	strWhere += " (ID LIKE '" + code + "%') AND (LENGTH(ID) = 8)";
                }
				
				mCursor = mDb.query("Category",
						new String[] { "ID", "NAME" },
						strWhere, null, null, null, "ID, NAME");
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}

			return mCursor;
		}
		
		public Cursor GetTeamList() throws SQLException
		{
			Cursor mCursor = null;
			try {
				String strWhere = "SYMBOL = 'LT' OR SYMBOL = 'SW' OR SYMBOL = 'NH' OR SYMBOL = 'HE'";
								
				mCursor = mDb.query("Category",
						new String[] { "ID", "NAME" },
						strWhere, null, null, null, "ID");
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}

			return mCursor;
		}
		
		public Cursor GetStoreList() throws SQLException
		{
			Cursor mCursor = null;
			try {
				String strWhere = "TYPEID ='CY01' OR TYPEID ='CY02'";
								
				mCursor = mDb.query("COMPANY",
						new String[] { "ID", "NAME" },
						strWhere, null, null, null, "TYPEID, NAME");
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}

			return mCursor;
		}
		
		public Cursor GetProductInfo(String strBarcode) throws SQLException
		{
			Cursor mCursor = null;
			try {
				String strWhere = "";
				
				strWhere += String.format("ID = '%s'", strBarcode);
				strWhere += String.format(" OR MAKERCODE = '%s'", strBarcode);
				strWhere += String.format(" OR LOGISTICSCODEA1 = '%s'", strBarcode);
				strWhere += String.format(" OR LOGISTICSCODEA2 = '%s'", strBarcode);
				strWhere += String.format(" OR LOGISTICSCODEB1 = '%s'", strBarcode);
				strWhere += String.format(" OR LOGISTICSCODEB2 = '%s'", strBarcode);
				strWhere += String.format(" OR LOGISTICSCODEC1 = '%s'", strBarcode);
				strWhere += String.format(" OR LOGISTICSCODEC2 = '%s'", strBarcode);
								
				mCursor = mDb.query("PRODUCT",
						new String[] { "*" },
						strWhere, null, null, null, null);
				
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}

			return mCursor;
		}
		
		public Cursor GetProductInfo(String strBarcode, String strProductOrModel) throws SQLException
		{
			Cursor mCursor = null;
			try {
				String strWhere = "";
				
				if(!strBarcode.trim().isEmpty())
				{
					strWhere += String.format("ID = '%s'", strBarcode.trim());
					strWhere += String.format(" OR MAKERCODE = '%s'", strBarcode.trim());
					strWhere += String.format(" OR LOGISTICSCODEA1 = '%s'", strBarcode.trim());
					strWhere += String.format(" OR LOGISTICSCODEA2 = '%s'", strBarcode.trim());
					strWhere += String.format(" OR LOGISTICSCODEB1 = '%s'", strBarcode.trim());
					strWhere += String.format(" OR LOGISTICSCODEB2 = '%s'", strBarcode.trim());
					strWhere += String.format(" OR LOGISTICSCODEC1 = '%s'", strBarcode.trim());
					strWhere += String.format(" OR LOGISTICSCODEC2 = '%s'", strBarcode.trim());
					
					strWhere += " AND (NAME LIKE '%" + strProductOrModel + "%'";
					strWhere += " OR MODEL LIKE '%" + strProductOrModel + "%')";
				}
				else
				{
					strWhere += " (NAME LIKE '%" + strProductOrModel + "%'";
					strWhere += " OR MODEL LIKE '%" + strProductOrModel + "%')"; 
				}
				
//				strWhere += String.format(" OR NAME like '_%s_'", strProductOrModel);
//				strWhere += String.format(" OR MODEL like '_%s_'", strProductOrModel);
								
				mCursor = mDb.query("PRODUCT",
						new String[] { "*" },
						strWhere, null, null, null, null);
				
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}

			return mCursor;
		}
		
		public Cursor GetStockCheckingItem(String id) throws SQLException
		{
			Cursor mCursor = null;
			try {
				String strWhere = "";
				
				if (id.length() > 0)
                {
					strWhere += " ID = '" + id + "'";
                }
				
				mCursor = mDb.query("ProductStockCheckingItem",
						new String[] { "*" },
						strWhere, null, null, null, "WORKDATE");
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}

			return mCursor;
		}
				
		public static List<HashMap<String, Object>> ConvertResultSetToList(Cursor cursor)
		{
			List<HashMap<String,Object>> list = new ArrayList<HashMap<String,Object>>();			
			int nColumns = cursor.getColumnCount();
			
			if(cursor == null)
				return list;
						
			if(!cursor.moveToFirst())
				return list;
			
			do
			{
				HashMap<String,Object> row = new HashMap<String, Object>(nColumns);
				
				for(int j=0; j < nColumns; ++j) {
					
			        row.put(cursor.getColumnName(j),cursor.getString(j));
			    }
			    list.add(row);
				
			}while(cursor.moveToNext());
			
			return list;
		}
		
		public Boolean LoginProcess(String id, String pw) throws SQLException
		{
			Boolean result = false;
			Cursor mCursor = null;
			try {
				mCursor = mDb.query("Staff",
						new String[] { 
								"USERID"},
						String.format(" USERID = '%s'", id) + String.format(" and PASSWD = '%s'", pw), null, null, null, null);
				
				if(mCursor != null)
				{
					if(mCursor.moveToFirst())
					{
						result = true;
					}
				}
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}

			return result;
		}
		
		public Boolean UpdateStockReplaceChecking(String id, String productqty, String totalqty)
		{
			Boolean result = false;
			
			ContentValues initialValues = new ContentValues();
			initialValues.put("TOTALPRODUCTQTY", productqty);
			initialValues.put("TOTALQTY", totalqty);
			
			String strWhere = String.format(" ID = '%s'", id);
			
			int nResult = mDb.update("ProductWarehouseTransfer", initialValues, strWhere, null);
			if(nResult > 0)
				result = true;
			
			return result;
		}
		
		public Boolean UpdateStockChecking(String id, String productqty, String totalqty)
		{
			Boolean result = false;
			
			ContentValues initialValues = new ContentValues();
			initialValues.put("TOTALPRODUCTQTY", productqty);
			initialValues.put("TOTALQTY", totalqty);
			
			String strWhere = String.format(" ID = '%s'", id);
			
			int nResult = mDb.update("ProductStockChecking", initialValues, strWhere, null);
			if(nResult > 0)
				result = true;
			
			return result;
		}
		
		public Boolean UpdateStockCheckingItem(DataRow dr, String strID)
		{
			Boolean result = false;
			
			ContentValues initialValues = new ContentValues();
			initialValues.put("QTY", dr.get("col4").toString());			
			
			String strWhere = String.format(" PRODUCTID = '%s' AND ID = '%s'", dr.get("col2").toString(), strID);
			
			int nResult = mDb.update("ProductStockCheckingItem", initialValues, strWhere, null);
			if(nResult > 0)
				result = true;
			
			return result;
		}
		
		public Boolean UpdateStockReplaceCheckingItem(DataRow dr, String strID)
		{
			Boolean result = false;
			
			ContentValues initialValues = new ContentValues();
			initialValues.put("QTY", dr.get("col3").toString());			
			
			String strWhere = String.format(" PRODUCTID = '%s' AND ID = '%s'", dr.get("col2").toString(), strID);
			
			int nResult = mDb.update("ProductWarehouseTransferItem", initialValues, strWhere, null);
			if(nResult > 0)
				result = true;
			
			return result;
		}
		
		public Boolean InsertUserInfo(String id, String pw, List listUserInfo)
		{
			Boolean result = false;
			
			if(GetLoginCount() == 0){
				for(Object h : listUserInfo)
				{
					HashMap<String, Object> hash = (HashMap)h;
					ContentValues initialValues = new ContentValues();
					initialValues.put("USERID", id);
					initialValues.put("PASSWD", pw);
					initialValues.put("NAME", hash.get("name").toString());
					initialValues.put("DBDATETIME", hash.get("dbdatetime").toString());
					initialValues.put("ID", hash.get("id").toString());
					
					long lResult = mDb.insert("Staff", null, initialValues);
					if(lResult != -1)
						result = true;
				}
			}
			else
			{
				for(Object h : listUserInfo)
				{
					HashMap<String, Object> hash = (HashMap)h;
					ContentValues initialValues = new ContentValues();
					initialValues.put("PASSWD", pw);
					initialValues.put("NAME", hash.get("name").toString());
					initialValues.put("DBDATETIME", hash.get("dbdatetime").toString());
					initialValues.put("ID", hash.get("id").toString());
					
					String strWhere = String.format(" USERID = '%s'", id);
					
					int nResult = mDb.update("Staff", initialValues, strWhere, null);
					if(nResult == 0)
					{						
						ContentValues initialValues1 = new ContentValues();
						initialValues1.put("USERID", id);
						initialValues1.put("PASSWD", pw);
						initialValues1.put("NAME", hash.get("name").toString());
						initialValues1.put("DBDATETIME", hash.get("dbdatetime").toString());
						initialValues1.put("ID", hash.get("id").toString());
						
						long lResult = mDb.insert("Staff", null, initialValues1);
						if(lResult != -1)
							result = true;
					}
				}
			}
			
			return result;
		}
		
		public Boolean InsertCategory(List listCategoryInfo)
		{
			Boolean result = false;
			
			if(GetLoginCount() == 0){
				for(Object h : listCategoryInfo)
				{
					HashMap<String, Object> hash = (HashMap)h;
					ContentValues initialValues = new ContentValues();
					initialValues.put("ID", hash.get("id").toString());
					initialValues.put("NAME", hash.get("name").toString());
					initialValues.put("SORTORDER", hash.get("sortorder").toString());
					initialValues.put("SYMBOL", hash.get("realsymbol").toString());
					initialValues.put("DEFAULTFLAG", hash.get("defaultflag").toString());
					initialValues.put("USEFLAG", hash.get("useflag").toString());
					
					long lResult = mDb.insert("Category", null, initialValues);
					if(lResult != -1)
						result = true;
				}
			}
			else
			{
				for(Object h : listCategoryInfo)
				{
					HashMap<String, Object> hash = (HashMap)h;
					ContentValues initialValues = new ContentValues();
					initialValues.put("NAME", hash.get("name").toString());
					initialValues.put("SORTORDER", hash.get("sortorder").toString());
					initialValues.put("SYMBOL", hash.get("realsymbol").toString());
					initialValues.put("DEFAULTFLAG", hash.get("defaultflag").toString());
					initialValues.put("USEFLAG", hash.get("useflag").toString());
					
					String strWhere = String.format(" ID = '%s'", hash.get("id").toString());
					
					int nResult = mDb.update("Category", initialValues, strWhere, null);
					if(nResult == 0)
					{						
						ContentValues initialValues1 = new ContentValues();
						initialValues1.put("ID", hash.get("id").toString());
						initialValues1.put("NAME", hash.get("name").toString());
						initialValues1.put("SORTORDER", hash.get("name").toString());
						initialValues1.put("SYMBOL", hash.get("realsymbol").toString());
						initialValues1.put("DEFAULTFLAG", hash.get("defaultflag").toString());
						initialValues1.put("USEFLAG", hash.get("useflag").toString());
						
						long lResult = mDb.insert("Category", null, initialValues1);
						if(lResult != -1)
							result = true;
					}
				}
			}
			
			return result;
		}
		
		public Boolean InsertCompany(List listCompanyInfo)
		{
			Boolean result = false;
			
			if(GetLoginCount() == 0){
				for(Object h : listCompanyInfo)
				{										
					HashMap<String, Object> hash = (HashMap)h;
					ContentValues initialValues = new ContentValues();
					initialValues.put("NAME", hash.get("name").toString());
					initialValues.put("BIZSERIAL", hash.get("bizserial").toString());
					initialValues.put("REPRESENTATIVE", hash.get("representative").toString());
					initialValues.put("PHONE", hash.get("phone").toString());
					initialValues.put("FAX", hash.get("fax").toString());
					initialValues.put("ADDRESS1", hash.get("address1").toString());
					initialValues.put("ADDRESS2", hash.get("address2").toString());
					initialValues.put("TYPEID", hash.get("typeid").toString());
					initialValues.put("ID", hash.get("id").toString());
					initialValues.put("PERIODDAY", hash.get("periodday").toString());
					
					long lResult = mDb.insert("Company", null, initialValues);
					if(lResult != -1)
						result = true;
				}
			}
			else
			{
				for(Object h : listCompanyInfo)
				{
					HashMap<String, Object> hash = (HashMap)h;
					ContentValues initialValues = new ContentValues();
					initialValues.put("NAME", hash.get("name").toString());
					initialValues.put("BIZSERIAL", hash.get("bizserial").toString());
					initialValues.put("REPRESENTATIVE", hash.get("representative").toString());
					initialValues.put("PHONE", hash.get("phone").toString());
					initialValues.put("FAX", hash.get("fax").toString());
					initialValues.put("ADDRESS1", hash.get("address1").toString());
					initialValues.put("ADDRESS2", hash.get("address2").toString());
					initialValues.put("TYPEID", hash.get("typeid").toString());
					initialValues.put("PERIODDAY", hash.get("periodday").toString());
					
					String strWhere = String.format(" ID = '%s'", hash.get("id").toString());
					
					int nResult = mDb.update("Company", initialValues, strWhere, null);
					if(nResult == 0)
					{						
						ContentValues initialValues1 = new ContentValues();
						initialValues1.put("NAME", hash.get("name").toString());
						initialValues1.put("BIZSERIAL", hash.get("bizserial").toString());
						initialValues1.put("REPRESENTATIVE", hash.get("representative").toString());
						initialValues1.put("PHONE", hash.get("phone").toString());
						initialValues1.put("FAX", hash.get("fax").toString());
						initialValues1.put("ADDRESS1", hash.get("address1").toString());
						initialValues1.put("ADDRESS2", hash.get("address2").toString());
						initialValues1.put("TYPEID", hash.get("typeid").toString());
						initialValues1.put("ID", hash.get("id").toString());
						initialValues1.put("PERIODDAY", hash.get("periodday").toString());
						
						long lResult = mDb.insert("Company", null, initialValues1);
						if(lResult != -1)
							result = true;
					}
				}
			}
			
			return result;
		}
		
		public Boolean InsertStockChecking(List listCompanyInfo)
		{
			Boolean result = false;
			
			for(Object h : listCompanyInfo)
			{										
				HashMap<String, Object> hash = (HashMap)h;
				ContentValues initialValues = new ContentValues();
				initialValues.put("ID", hash.get("ID").toString());
				initialValues.put("WAREHOUSEID", hash.get("WAREHOUSEID").toString());
				initialValues.put("TOTALPRODUCTQTY", hash.get("TOTALPRODUCTQTY").toString());
				initialValues.put("TOTALQTY", hash.get("TOTALQTY").toString());
				initialValues.put("MEMO", hash.get("MEMO").toString());
				initialValues.put("WORKDATE", hash.get("WORKDATE").toString());
				initialValues.put("INSERTSTAFF", hash.get("INSERTSTAFF").toString());
				
				long lResult = mDb.insert("ProductStockChecking", null, initialValues);
				if(lResult != -1)
					result = true;
			}
			
			return result;
		}
		
		public Boolean InsertStockCheckingItem(List listCompanyInfo) throws SQLException
		{
			Boolean result = false;
			
			try
			{
				for(Object h : listCompanyInfo)
				{										
					HashMap<String, Object> hash = (HashMap)h;
					ContentValues initialValues = new ContentValues();
					initialValues.put("ID", hash.get("ID").toString());
					initialValues.put("WAREHOUSEID", hash.get("WAREHOUSEID").toString());
					initialValues.put("PRODUCTID", hash.get("PRODUCTID").toString());
					initialValues.put("QTY", hash.get("QTY").toString());
					initialValues.put("WORKDATE", hash.get("WORKDATE").toString());
					initialValues.put("WORKERID", hash.get("WORKERID").toString());
					initialValues.put("PRODUCTNAME", hash.get("PRODUCTNAME").toString());
					
					long lResult = mDb.insert("ProductStockCheckingItem", null, initialValues);
					
					if(lResult == -1)
					{						
						ContentValues initialValues1 = new ContentValues();
						initialValues1.put("WAREHOUSEID", hash.get("WAREHOUSEID").toString());
						initialValues1.put("PRODUCTID", hash.get("PRODUCTID").toString());
						initialValues1.put("QTY", hash.get("QTY").toString());
						initialValues1.put("WORKDATE", hash.get("WORKDATE").toString());
						initialValues1.put("WORKERID", hash.get("WORKERID").toString());
						initialValues1.put("PRODUCTNAME", hash.get("PRODUCTNAME").toString());
						
						String strWhere = String.format(" ID = '%s'", hash.get("ID").toString());
						
						int nResult = mDb.update("ProductStockCheckingItem", initialValues1, strWhere, null);
						if(nResult > 0)
							result = true;
					}
					else
						result = true;
					
				}
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
			
			return result;
		}
		
		public Boolean InsertStockReplaceItem(List listCompanyInfo) throws SQLException
		{
			Boolean result = false;
			
			try
			{
				for(Object h : listCompanyInfo)
				{										
					HashMap<String, Object> hash = (HashMap)h;
					ContentValues initialValues = new ContentValues();
					initialValues.put("ID", hash.get("ID").toString());
					initialValues.put("PRODUCTID", hash.get("PRODUCTID").toString());
					initialValues.put("QTY", hash.get("QTY").toString());
					initialValues.put("WORKDATE", hash.get("WORKDATE").toString());
					initialValues.put("WORKERID", hash.get("WORKERID").toString());
					
					long lResult = mDb.insert("ProductWarehouseTransferItem", null, initialValues);
					
					if(lResult == -1)
					{						
						ContentValues initialValues1 = new ContentValues();
						initialValues1.put("PRODUCTID", hash.get("PRODUCTID").toString());
						initialValues1.put("QTY", hash.get("QTY").toString());
						initialValues1.put("WORKDATE", hash.get("WORKDATE").toString());
						initialValues1.put("WORKERID", hash.get("WORKERID").toString());
						
						String strWhere = String.format(" ID = '%s'", hash.get("ID").toString());
						
						int nResult = mDb.update("ProductWarehouseTransferItem", initialValues1, strWhere, null);
						if(nResult > 0)
							result = true;
					}
					else
						result = true;
					
				}
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
			
			return result;
		}
		
		public Boolean InsertStockReplace(List listCompanyInfo)
		{
			Boolean result = false;
			
			for(Object h : listCompanyInfo)
			{										
				HashMap<String, Object> hash = (HashMap)h;
				ContentValues initialValues = new ContentValues();
				initialValues.put("ID", hash.get("ID").toString());
				initialValues.put("FROMWAREHOUSEID", hash.get("FROMWAREHOUSEID").toString());
				initialValues.put("TOWAREHOUSEID", hash.get("TOWAREHOUSEID").toString());
				initialValues.put("TOTALPRODUCTQTY", hash.get("TOTALPRODUCTQTY").toString());
				initialValues.put("TOTALQTY", hash.get("TOTALQTY").toString());
				initialValues.put("MEMO", hash.get("MEMO").toString());
				initialValues.put("WORKDATE", hash.get("WORKDATE").toString());
				initialValues.put("STAFFID", hash.get("STAFFID").toString());
				
				long lResult = mDb.insert("ProductWarehouseTransfer", null, initialValues);
				if(lResult != -1)
					result = true;
			}
			
			return result;
		}
		
		public Boolean InsertItem(List listProductInfo)
		{
			Boolean result = false;
			
			if(GetLoginCount() == 0){
				for(Object h : listProductInfo)
				{										
					HashMap<String, Object> hash = (HashMap)h;
					ContentValues initialValues = new ContentValues();
					initialValues.put("ID", hash.get("id").toString());
					initialValues.put("MAKERCODE", hash.get("makercode").toString());
					initialValues.put("NAME", hash.get("name").toString());
					initialValues.put("CATEGORYID", hash.get("categoryid").toString());
					initialValues.put("PRICE", hash.get("price").toString());
					initialValues.put("ORGPRICE", hash.get("orgprice").toString());
					initialValues.put("MANUFACTURERID", hash.get("manufacturerid").toString());
					initialValues.put("MODEL", hash.get("model").toString());
					initialValues.put("SPECSIZEID", hash.get("specsizeid").toString());
					initialValues.put("TEAMID", hash.get("teamid").toString());
					initialValues.put("BRANDID", hash.get("brandid").toString());
					initialValues.put("COLOR", hash.get("color").toString());
					initialValues.put("MATERIAL", hash.get("material").toString());
					initialValues.put("LOGISTICSCODEA1", hash.get("logisticscodea1").toString());
					initialValues.put("LOGISTICSCODEA2", hash.get("logisticscodea2").toString());
					initialValues.put("LOGISTICSUNITA", hash.get("logisticsunita").toString());
					initialValues.put("LOGISTICSCODEB1", hash.get("logisticscodeb1").toString());
					initialValues.put("LOGISTICSCODEB2", hash.get("logisticscodeb2").toString());
					initialValues.put("LOGISTICSUNITB", hash.get("logisticsunitb").toString());
					initialValues.put("LOGISTICSCODEC1", hash.get("logisticscodec1").toString());
					initialValues.put("LOGISTICSCODEC2", hash.get("logisticscodec2").toString());
					initialValues.put("LOGISTICSUNITC", hash.get("logisticsunitc").toString());
					initialValues.put("MEMO", hash.get("memo").toString());
					
					long lResult = mDb.insert("Product", null, initialValues);
					if(lResult != -1)
						result = true;
				}
			}
			else
			{
				for(Object h : listProductInfo)
				{
					HashMap<String, Object> hash = (HashMap)h;
					ContentValues initialValues = new ContentValues();
					initialValues.put("MAKERCODE", hash.get("makercode").toString());
					initialValues.put("NAME", hash.get("name").toString());
					initialValues.put("CATEGORYID", hash.get("categoryid").toString());
					initialValues.put("PRICE", hash.get("price").toString());
					initialValues.put("ORGPRICE", hash.get("orgprice").toString());
					initialValues.put("MANUFACTURERID", hash.get("manufacturerid").toString());
					initialValues.put("MODEL", hash.get("model").toString());
					initialValues.put("SPECSIZEID", hash.get("specsizeid").toString());
					initialValues.put("TEAMID", hash.get("teamid").toString());
					initialValues.put("BRANDID", hash.get("brandid").toString());
					initialValues.put("COLOR", hash.get("color").toString());
					initialValues.put("MATERIAL", hash.get("material").toString());
					initialValues.put("LOGISTICSCODEA1", hash.get("logisticscodea1").toString());
					initialValues.put("LOGISTICSCODEA2", hash.get("logisticscodea2").toString());
					initialValues.put("LOGISTICSUNITA", hash.get("logisticsunita").toString());
					initialValues.put("LOGISTICSCODEB1", hash.get("logisticscodeb1").toString());
					initialValues.put("LOGISTICSCODEB2", hash.get("logisticscodeb2").toString());
					initialValues.put("LOGISTICSUNITB", hash.get("logisticsunitb").toString());
					initialValues.put("LOGISTICSCODEC1", hash.get("logisticscodec1").toString());
					initialValues.put("LOGISTICSCODEC2", hash.get("logisticscodec2").toString());
					initialValues.put("LOGISTICSUNITC", hash.get("logisticsunitc").toString());
					initialValues.put("MEMO", hash.get("memo").toString());
					
					String strWhere = String.format(" ID = '%s'", hash.get("id").toString());
					
					int nResult = mDb.update("Product", initialValues, strWhere, null);
					if(nResult == 0)
					{						
						ContentValues initialValues1 = new ContentValues();
						initialValues1.put("ID", hash.get("id").toString());
						initialValues1.put("MAKERCODE", hash.get("makercode").toString());
						initialValues1.put("NAME", hash.get("name").toString());
						initialValues1.put("CATEGORYID", hash.get("categoryid").toString());
						initialValues1.put("PRICE", hash.get("price").toString());
						initialValues1.put("ORGPRICE", hash.get("orgprice").toString());
						initialValues1.put("MANUFACTURERID", hash.get("manufacturerid").toString());
						initialValues1.put("MODEL", hash.get("model").toString());
						initialValues1.put("SPECSIZEID", hash.get("specsizeid").toString());
						initialValues1.put("TEAMID", hash.get("teamid").toString());
						initialValues1.put("BRANDID", hash.get("brandid").toString());
						initialValues1.put("COLOR", hash.get("color").toString());
						initialValues1.put("MATERIAL", hash.get("material").toString());
						initialValues1.put("LOGISTICSCODEA1", hash.get("logisticscodea1").toString());
						initialValues1.put("LOGISTICSCODEA2", hash.get("logisticscodea2").toString());
						initialValues1.put("LOGISTICSUNITA", hash.get("logisticsunita").toString());
						initialValues1.put("LOGISTICSCODEB1", hash.get("logisticscodeb1").toString());
						initialValues1.put("LOGISTICSCODEB2", hash.get("logisticscodeb2").toString());
						initialValues1.put("LOGISTICSUNITB", hash.get("logisticsunitb").toString());
						initialValues1.put("LOGISTICSCODEC1", hash.get("logisticscodec1").toString());
						initialValues1.put("LOGISTICSCODEC2", hash.get("logisticscodec2").toString());
						initialValues1.put("LOGISTICSUNITC", hash.get("logisticsunitc").toString());
						initialValues1.put("MEMO", hash.get("memo").toString());						
						long lResult = mDb.insert("Product", null, initialValues1);
						if(lResult != -1)
							result = true;
					}
					else
						result = true;
				}
			}
			
			return result;
		}
		
		public Cursor GetVersion()
		{
			Cursor mCursor = null;
			
			try {
				mCursor = mDb.query("Version",
						new String[] { 
								"VERSIONID"},
						null, null, null, null, null);
				
				if(mCursor != null && mCursor.getCount() > 0)
				{
					mCursor.moveToFirst();
					return mCursor;
				}
				else
				{
					ContentValues initialValues = new ContentValues();
					initialValues.put("VERSIONID", "20120220000000");
					
					long lResult = mDb.insert("Version", null, initialValues);
					if(lResult != -1)
					{
						mCursor = mDb.query("Version",
								new String[] { 
										"VERSIONID"},
								null, null, null, null, null);
						if(mCursor != null)
						{
							mCursor.moveToFirst();
						}
					}
				}
				
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			
			return mCursor;
		}

}
