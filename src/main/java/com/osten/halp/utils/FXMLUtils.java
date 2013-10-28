/*
 * Copyright 2013 SmartBear Software
 * 
 * Licensed under the EUPL, Version 1.1 or - as soon they will be approved by the European Commission - subsequent
 * versions of the EUPL (the "Licence");
 * You may not use this work except in compliance with the Licence.
 * You may obtain a copy of the Licence at:
 * 
 * http://ec.europa.eu/idabc/eupl
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the Licence is
 * distributed on an "AS IS" basis, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the Licence for the specific language governing permissions and limitations
 * under the Licence.
 */
package com.osten.halp.utils;

import java.io.IOException;
import java.net.URL;
import java.util.Collections;
import java.util.Map;

import javafx.fxml.FXMLLoader;

import javafx.scene.Node;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FXMLUtils
{
    protected static final Logger log = LoggerFactory.getLogger( FXMLUtils.class );

    public static void load(Node root){
        FXMLLoader fxmlLoader = new FXMLLoader(root.getClass().getResource(
                root.getClass().getSimpleName() + ".fxml"));
        fxmlLoader.setRoot(root);
        fxmlLoader.setController(root);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

}