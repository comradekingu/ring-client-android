/*
 *  Copyright (C) 2004-2014 Savoir-Faire Linux Inc.
 *
 *  Author: Alexandre Lision <alexandre.lision@savoirfairelinux.com>
 *
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *   Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 *
 *  Additional permission under GNU GPL version 3 section 7:
 *
 *  If you modify this program, or any covered work, by linking or
 *  combining it with the OpenSSL project's OpenSSL library (or a
 *  modified version of that library), containing parts covered by the
 *  terms of the OpenSSL or SSLeay licenses, Savoir-Faire Linux Inc.
 *  grants you additional permission to convey the resulting work.
 *  Corresponding Source for a non-source form of such a combination
 *  shall include the source code for the parts of OpenSSL used as well
 *  as that of the covered work.
 */


package org.sflphone.history;

import android.os.Parcel;
import android.os.Parcelable;
import com.j256.ormlite.field.DatabaseField;
import org.sflphone.service.ServiceConstants;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.TimeZone;

public class HistoryCall implements Parcelable {

    public long getCallStart() {
        return call_start;
    }

    @DatabaseField(index = true)
    long call_start;
    @DatabaseField
    long call_end;
    @DatabaseField
    String number;
    @DatabaseField
    boolean missed;
    @DatabaseField
    String direction;
    @DatabaseField
    String recordPath;
    @DatabaseField
    String timeFormatted;

    public String getAccountID() {
        return accountID;
    }

    @DatabaseField
    String accountID;

    public long getContactID() {
        return contactID;
    }

    @DatabaseField
    long contactID;
    @DatabaseField
    long callID;

    /* Needed by ORMLite */
    public HistoryCall() {
    }

    public String getDirection() {
        return direction;
    }

    public String getDate() {
        return timeFormatted;
    }

    public String getStartString(String format) {
        Timestamp stamp = new Timestamp(call_start * 1000); // in milliseconds
        Date date = new Date(stamp.getTime());
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.getDefault());
        sdf.setTimeZone(TimeZone.getDefault());
        String formattedDate = sdf.format(date);
        return formattedDate;

    }

    public String getDurationString() {

        long duration = call_end - call_start;
        if (duration < 60)
            return String.format(Locale.getDefault(), "%02d secs", duration);

        if (duration < 3600)
            return String.format(Locale.getDefault(), "%02d mins %02d secs", (duration % 3600) / 60, (duration % 60));

        return String.format(Locale.getDefault(), "%d h %02d mins %02d secs", duration / 3600, (duration % 3600) / 60, (duration % 60));

    }

    public long getDuration() {
        return call_end - call_start;
    }

    public String getRecordPath() {
        return recordPath;
    }

    public String getNumber() {
        return number;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(call_start);
        dest.writeLong(call_end);
        dest.writeString(accountID);
        dest.writeString(number);
        dest.writeByte((byte) (missed ? 1 : 0));
        dest.writeString(direction);
        dest.writeString(recordPath);
        dest.writeString(timeFormatted);
        dest.writeLong(contactID);
        dest.writeLong(callID);
    }

    public static final Parcelable.Creator<HistoryCall> CREATOR = new Parcelable.Creator<HistoryCall>() {
        public HistoryCall createFromParcel(Parcel in) {
            return new HistoryCall(in);
        }

        public HistoryCall[] newArray(int size) {
            return new HistoryCall[size];
        }
    };

    private HistoryCall(Parcel in) {
        call_start = in.readLong();
        call_end = in.readLong();
        accountID = in.readString();
        number = in.readString();
        missed = in.readByte() == 1 ? true : false;
        direction = in.readString();
        recordPath = in.readString();
        timeFormatted = in.readString();
        contactID = in.readLong();
        callID = in.readLong();
    }

    public boolean hasRecord() {
        return recordPath.length() > 0;
    }

    public boolean isIncoming() {
        return true;
    }

    public boolean isMissed() {
        return missed;
    }

}