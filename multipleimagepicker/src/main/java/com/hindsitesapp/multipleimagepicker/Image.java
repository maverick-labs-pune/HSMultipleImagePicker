/*
 * ************************************************************************
 *
 *  MAVERICK LABS CONFIDENTIAL
 *  __________________
 *
 *   [2015] Maverick Labs
 *   All Rights Reserved.
 *
 *  NOTICE:  All information contained herein is, and remains
 *  the property of Maverick Labs and its suppliers,
 *  if any.  The intellectual and technical concepts contained
 *  herein are proprietary to Maverick Labs
 *  and its suppliers and may be covered by U.S. and Foreign Patents,
 *  patents in process, and are protected by trade secret or copyright law.
 *  Dissemination of this information or reproduction of this material
 *  is strictly forbidden unless prior written permission is obtained
 *  from Maverick Labs.
 * /
 */

package com.hindsitesapp.multipleimagepicker;

/**
 * Created by Authoritah on 2/11/2015.
 */
public class Image {
    public String path;
    public String name;
    public long time;

    public Image(String path, String name, long time){
        this.path = path;
        this.name = name;
        this.time = time;
    }

    @Override
    public boolean equals(Object o) {
        try {
            Image other = (Image) o;
            return this.path.equalsIgnoreCase(other.path);
        }catch (ClassCastException e){
            e.printStackTrace();
        }
        return super.equals(o);
    }
}