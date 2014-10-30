package org.apache.maven.shared.project.utils;

/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

import java.io.File;
import java.util.Map;

import org.apache.maven.model.Model;
import org.apache.maven.project.MavenProject;

/**
 * 
 */
public final class SiteUtils
{

    private SiteUtils()
    {
    }

    public static String resolveDistributionManagementSiteUrl( MavenProject project )
    {
        return resolveDistributionManagementSiteUrl( project, true );
    }
    
    /**
     * 
     * @param project
     * @param useModuleName use the moduleName instead of the artifactId
     * @return
     */
    public static String resolveDistributionManagementSiteUrl( MavenProject project, boolean useModuleName )
    {
        String siteUrl = getDistributionManagementSiteUrl( project.getModel() );

        if ( siteUrl == null )
        {
            // prevent null-value
            siteUrl = String.valueOf( getDistributionManagementSiteUrl( project ) );
            if ( !ProjectUtils.isRootProject( project ) )
            {
                if ( useModuleName )
                {
                    Map<String, String> modules = ProjectUtils.getAllModules( project.getParent() );
                    
                    for ( String module : modules.keySet() )
                    {
                        if ( new File( project.getParent().getBasedir(), module ).equals( project.getFile() ) )
                        {
                            return siteUrl + '/' + module;
                        }
                    }
                    // project is not a module of its parent, so use project's directoryname
                    siteUrl += '/' + project.getFile().getParentFile().getName();
                }
                else
                {
                    siteUrl += '/' + project.getArtifactId();
                }
            }
        }
        return siteUrl;
    }
    
    /**
     * 
     * @param model
     * @return return the url if available, otherwise {@code null} 
     */
    protected static String getDistributionManagementSiteUrl( Model model )
    {
        if ( model.getDistributionManagement() == null || model.getDistributionManagement().getSite() == null )
        {
            return null;
        }
        return model.getDistributionManagement().getSite().getUrl();
    }

    protected static String getDistributionManagementSiteUrl( MavenProject project )
    {
        if ( project.getDistributionManagement() == null || project.getDistributionManagement().getSite() == null )
        {
            return null;
        }
        return project.getDistributionManagement().getSite().getUrl();
    }

}
