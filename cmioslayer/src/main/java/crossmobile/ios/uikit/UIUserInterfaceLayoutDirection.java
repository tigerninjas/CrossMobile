/*
 * (c) 2020 by Panayotis Katsaloulis
 *
 * CrossMobile is free software; you can redistribute it and/or modify
 * it under the terms of the CrossMobile Community License as published
 * by the CrossMobile team.
 *
 * CrossMobile is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * CrossMobile Community License for more details.
 *
 * You should have received a copy of the CrossMobile Community
 * License along with CrossMobile; if not, please contact the
 * CrossMobile team at https://crossmobile.tech/contact/
 */
package crossmobile.ios.uikit;

import org.crossmobile.bridge.ann.CMEnum;

/**
 * UIUserNotificationType class defines  the layout alignment of the
 * elements based on the language settings.
 */
@CMEnum
public final class UIUserInterfaceLayoutDirection {
    /**
     * Direction is left to right.
     */
    public static final int LeftToRight = 0;

    /**
     * Direction is right to left.
     */
    public static final int RightToLeft = 1;


    private UIUserInterfaceLayoutDirection() {
    }
}
