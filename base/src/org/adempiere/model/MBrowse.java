/******************************************************************************
 * Product: ADempiere ERP & CRM Smart Business Solution                       *
 * Copyright (C) 2003-2011 e-Evolution Consultants. All Rights Reserved.      *
 * Copyright (C) 2003-2011 Victor Pérez Juárez 								  * 
 * This program is free software; you can redistribute it and/or modify it    *
 * under the terms version 2 of the GNU General Public License as published   *
 * by the Free Software Foundation. This program is distributed in the hope   *
 * that it will be useful, but WITHOUT ANY WARRANTY; without even the implied *
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.           *
 * See the GNU General Public License for more details.                       *
 * You should have received a copy of the GNU General Public License along    *
 * with this program; if not, write to the Free Software Foundation, Inc.,    *
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA.                     *
 * For the text or an alternative of this public license, you may reach us    *
 * Contributor(s): Victor Pérez Juárez  (victor.perez@e-evolution.com)		  *
 * Sponsors: e-Evolution Consultants (http://www.e-evolution.com/)            *
 *****************************************************************************/
package org.adempiere.model;

import java.sql.ResultSet;
import java.util.List;
import java.util.Properties;

import org.compiere.model.Query;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.Env;

/**
 * Class Model for Smart Browse
 * 
 * @author victor.perez@e-evoluton.com, www.e-evolution.com
 *  <li>FR [ 3426137 ] Smart Browser
 * 	https://sourceforge.net/tracker/?func=detail&aid=3426137&group_id=176962&atid=879335
 * 
 */
public class MBrowse extends X_AD_Browse {


	/**
	 * 
	 */
	private static final long serialVersionUID = -7723306980903810620L;
	/** Logger */
	private static CLogger s_log = CLogger.getCLogger(MBrowse.class);
	private MView m_view = null;
	private String m_title = null;

	/**************************************************************************
	 * Asset Constructor
	 * 
	 * @param ctx
	 *            context
	 * @param AD_SmartBrowse_ID
	 *            InOutBound ID
	 * @param trxName
	 *            transaction name
	 */
	public MBrowse(Properties ctx, int AD_SmartBrowse_ID, String trxName) {
		super(ctx, AD_SmartBrowse_ID, trxName);
		if (AD_SmartBrowse_ID == 0) {
		}
	}

	/**
	 * Discontinued Asset Constructor - DO NOT USE (but don't delete either)
	 * 
	 * @param ctx
	 *            context
	 * @param AD_SmartBrowse_ID
	 *            Cahs Flow ID
	 */
	public MBrowse(Properties ctx, int AD_SmartBrowse_ID) {
		this(ctx, AD_SmartBrowse_ID, null);
	}

	/**
	 * Load Constructor
	 * 
	 * @param ctx
	 *            context
	 * @param rs
	 *            result set record
	 * @param trxName
	 *            transaction
	 */
	public MBrowse(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	} //

	/**
	 * String representation
	 * 
	 * @return info
	 */
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer(" MSmartBrowse[").append(get_ID())
				.append("-").append(getName()).append("]");
		return sb.toString();
	} // toString

	/**
	 * get Criteria Fields
	 * 
	 * @return List Browse field
	 */
	public List<MBrowseField> getCriteriaFields() {

		String whereClause = MBrowseField.COLUMNNAME_AD_Browse_ID + "=? AND "
				+ MBrowseField.COLUMNNAME_IsQueryCriteria + "=?";

		return new Query(getCtx(), MBrowseField.Table_Name, whereClause,
				get_TrxName()).setParameters(get_ID(),"Y")
				.setOnlyActiveRecords(true)
				.setOrderBy(MBrowseField.COLUMNNAME_SeqNo).list();
	}
	
	/**
	 * get Criteria Fields
	 * 
	 * @return List Fields
	 */
	public List<MBrowseField> getFields() {

		String whereClause = MBrowseField.COLUMNNAME_AD_Browse_ID + "=? AND "
				+ MBrowseField.COLUMNNAME_IsDisplayed + "=? ";
		return new Query(getCtx(), MBrowseField.Table_Name, whereClause,
				get_TrxName()).setParameters(get_ID(), "Y")
				.setOnlyActiveRecords(true)
				.setOrderBy(MBrowseField.COLUMNNAME_SeqNo).list();
	}
	
	/**
	 * get Fields is not ReadOnly
	 * 
	 * @return List Fields Update
	 */
	public List<MBrowseField> getIsNotReadOnlyFields() {

		String whereClause = MBrowseField.COLUMNNAME_AD_Browse_ID + "=? AND "
						   + MBrowseField.COLUMNNAME_IsDisplayed + "=? AND "
				           + MBrowseField.COLUMNNAME_IsReadOnly + "=? ";
		return new Query(getCtx(), MBrowseField.Table_Name, whereClause,
				get_TrxName()).setParameters(get_ID(), "Y", "N")
				.setOnlyActiveRecords(true)
				.setOrderBy(MBrowseField.COLUMNNAME_SeqNo)
				.list();
	}

	/**
	 * get field using name
	 * 
	 * @param name field
	 * @return field
	 */
	public MBrowseField getField(String name) {
		for (MBrowseField field : getFields()) {
			if (field.getName().equals(name)) {
				return field;
			}
		}
		return null;
	}
	
	/**
	 * get field Key
	 * 
	 * @param name
	 *            field
	 * @return field
	 */
	public MBrowseField getFieldKey() {
		for (MBrowseField field : getFields()) {
			if (field.isKey()) {
				return field;
			}
		}
		return null;
	}

	/**
	 * get MView
	 */
	public MView getAD_View() {
		if (m_view == null) {
			m_view = new MView(getCtx(), getAD_View_ID(), get_TrxName());
		}
		return m_view;
	}
	
	/**
	 * 	Before Delete
	 *	@return true of it can be deleted
	 */
	protected boolean beforeDelete ()
	{
		DB.executeUpdate("DELETE FROM AD_Browse_Access WHERE AD_Browse_ID=? ", getAD_Browse_ID(),get_TrxName());
		DB.executeUpdate("DELETE FROM AD_Browse_Trl WHERE AD_Browse_ID=? ", getAD_Browse_ID(),get_TrxName());
		return true;
	}	//	beforeDelete
	
	public String getTitle()
	{
		if(m_title != null)
			return m_title;
		
		final boolean baseLanguage = Env.isBaseLanguage(Env.getCtx(),
				"AD_Browse");
		final String sql = "SELECT Name FROM AD_Browse_Trl WHERE AD_Browse_ID=? AND AD_LANGUAGE=?";
		m_title = baseLanguage ?getName() : DB.getSQLValueString(null,
				sql, getAD_Browse_ID(),
				Env.getAD_Language(Env.getCtx()));
		return m_title;
	}
}
