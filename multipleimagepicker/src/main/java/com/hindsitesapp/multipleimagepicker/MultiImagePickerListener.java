/*
 *  /*************************************************************************
 *  *
 *  * MAVERICK LABS CONFIDENTIAL
 *  * __________________
 *  *
 *  *  [2015] Maverick Labs
 *  *  All Rights Reserved.
 *  *
 *  * NOTICE:  All information contained herein is, and remains
 *  * the property of Maverick Labs and its suppliers,
 *  * if any.  The intellectual and technical concepts contained
 *  * herein are proprietary to Maverick Labs
 *  * and its suppliers and may be covered by U.S. and Foreign Patents,
 *  * patents in process, and are protected by trade secret or copyright law.
 *  * Dissemination of this information or reproduction of this material
 *  * is strictly forbidden unless prior written permission is obtained
 *  * from Maverick Labs.
 *
 */
package com.hindsitesapp.multipleimagepicker;

import java.io.Serializable;
import java.util.List;

/**
 *  Interface created to provide callback if systemVisibility changed to immersive
 *  Created by Authoritah on 9/22/2015.
 */
public interface MultiImagePickerListener extends Serializable {
    void onImagesPicked(List<PickedPhoto> pickedPhotos);
}
