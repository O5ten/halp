package com.osten.halp.api.model.gui;

import com.osten.halp.api.model.shared.DataModel;
import com.osten.halp.api.model.shared.FilterModel;
import com.osten.halp.api.model.shared.ProfileModel;

/**
 * Created with IntelliJ IDEA.
 * User: osten
 * Date: 10/31/13
 * Time: 2:34 PM
 * To change this template use File | Settings | File Templates.
 */
public interface PopulatableView<Type>
{
	public void populate( DataModel<Type> properties, FilterModel<Type> filterModel, ProfileModel<Type> profileModel);
	public DataModel<Type> getPropertyModel();
}
