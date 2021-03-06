/*
 * Copyright 2017-2019 Amazon.com, Inc. or its affiliates. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License").
 * You may not use this file except in compliance with the License.
 * A copy of the License is located at
 *
 *     http://aws.amazon.com/apache2.0/
 *
 * or in the "license" file accompanying this file. This file is distributed
 * on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing
 * permissions and limitations under the License.
 */

package com.amazon.aace.localVoiceControl.config;

import android.util.Log;

import com.amazon.aace.core.config.EngineConfiguration;
import com.amazon.aace.core.config.StreamConfiguration;

import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

/**
 * Factory interface for configuring parameters for local voice control. The application
 * should use only one of the two methods provided to configure parameters.
 */
public class LocalVoiceControlConfiguration {

    private static final String sTag = LocalVoiceControlConfiguration.class.getSimpleName();

    public static enum SocketPermission {
        OWNER,
        GROUP,
        ALL
    }

    /**
     * Factory method used to programmatically generate an engine configuration for local voice control
     * using a file path. The contents of the file referenced by the path should match the configuration
     * file used to launch the local Alexa endpoint. The developer must ensure that the application
     * has permissions to read the file.
     *
     * The data generated is equivalent to providing the following JSON values in a configuration file:
     *
     * @code{.json}
     * {
     *   "lvc" :
     *    {
     *       "configFilePath" : "<PATH TO THE CONFIGURATION FILE>"
     *    }
     * }
     * @endcode
     *
     * @param configFilePath Path to the configuration file where execution controller and platform services
     *                          socket directories are defined.
     */
    public static EngineConfiguration createFileConfig(String configFilePath) {
        EngineConfiguration config = null;

        try {

            // Create object with file path
            JSONObject element = new JSONObject();

            element.put("configFilePath", configFilePath );

            // Create high level objects and add children
            JSONObject jsonConfig = new JSONObject();
            jsonConfig.put("lvc", element);

            // Create input stream from JSON object
            String configStr = jsonConfig.toString();
            InputStream is = new ByteArrayInputStream(configStr.getBytes(StandardCharsets.UTF_8.name()));

            // Create engine configuration stream
            config = StreamConfiguration.create( is );
        } catch( Throwable ex ) {
            Log.e( sTag, ex.getMessage() );
        }

        return config;
    }

   /**
     * Factory method used to programmatically generate an engine configuration for Local Voice Control.
     * This method allows passing values for the specified parameters directly. The data generated by this method
     * is equivalent to providing the following JSON values in a configuration file:
     *
     * @code{.json}
     * {
     *   "lvc":
     *   {
     *     "controllerSocketDirectory"    : "<SOCKET_DIRECTORY_PATH_FOR_EXECUTION_CONTROLLER>",
     *     "controllerSocketPermissions"  : "<SOCKET_PERMISSION>",
     *     "platformSocketDirectory"      : "<SOCKET_DIRECTORY_PATH_FOR_PLATFORM_SERVICES>",
     *     "platformSocketPermissions"    : "<SOCKET_PERMISSION>",
     *     "address"                      : "<LOCAL_ALEXA_ENDPOINT_ADDRESS>",
     *     "messageRouterSocketDirectory" : "<SOCKET_DIRECTORY_PATH_FOR_MESSAGE_ROUTER>"
     *   }
     * }
     * @endcode
     *
     * @param [in] controllerSocketDirectory    The directory path where file socket will be created for execution controller.
     * @param [in] controllerSocketPermissions  The permissions for the file socket.
     *             Supported values:
     *                 @c OWNER - sets / checks the permission for owner only (600 for socket, 500 for directory)
     *                 @c GROUP - sets / checks the permission for owner and group (660 for socket, 550 for directory)
     *                 @c ALL - sets / checks the permission for owner, group, and other (666 for socket, 555 for directory)
     * @param [in] platformSocketDirectory      The directory path where file socket will be created for platform services.
     * @param [in] platformSocketPermissions    The permissions for the file socket.
     *             Supported values:
     *                 @c OWNER - sets / checks the permission for owner only (600 for socket, 500 for directory)
     *                 @c GROUP - sets / checks the permission for owner and group (660 for socket, 550 for directory)
     *                 @c ALL - sets / checks the permission for owner, group, and other (666 for socket, 555 for directory)
     * @param [in] address                      The IP address of the local Alexa endpoint.
     * @param [in] messageRouterSocketDirectory The directory path to the socket file for hybrid message router and local Alexa endpoint connection
     */
    public static EngineConfiguration createIPCConfig( String controllerSocketDirectory, SocketPermission controllerSocketPermissions,
                                                       String platformSocketDirectory, SocketPermission platformSocketPermissions,
                                                       String address, String messageRouterSocketDirectory ) {
        EngineConfiguration config = null;

        try {

            // Create object with directory path and permissions
            JSONObject element = new JSONObject();

            element.put("controllerSocketDirectory"    , controllerSocketDirectory          );
            element.put("controllerSocketPermissions"  , controllerSocketPermissions.name() );
            element.put("platformSocketDirectory"      , platformSocketDirectory            );
            element.put("platformSocketPermissions"    , platformSocketPermissions.name()   );
            element.put("address"                      , address                            );
            element.put("messageRouterSocketDirectory" , messageRouterSocketDirectory       );

            // Create high level objects and add children
            JSONObject jsonConfig = new JSONObject();
            jsonConfig.put("lvc", element);

            // Create input stream from JSON object
            String configStr = jsonConfig.toString();
            InputStream is = new ByteArrayInputStream(configStr.getBytes(StandardCharsets.UTF_8.name()));

            // Create engine configuration stream
            config = StreamConfiguration.create( is );
        } catch( Throwable ex ) {
            Log.e( sTag, ex.getMessage() );
        }

        return config;
    }

}
