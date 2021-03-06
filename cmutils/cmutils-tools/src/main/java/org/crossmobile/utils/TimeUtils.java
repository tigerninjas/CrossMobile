// (c) 2020 by Panayotis Katsaloulis
// SPDX-License-Identifier: LGPL-3.0-only

package org.crossmobile.utils;

import org.crossmobile.bridge.system.BaseUtils;

public class TimeUtils {


    public static void time(Procedure r, String description) {
        timeImpl(() -> {
            r.run();
            return null;
        }, description);
    }

    public static <R> R time(ProcedureR<R> r, String description) {
        return timeImpl(r, description);
    }

    private static <R> R timeImpl(ProcedureR<R> r, String description) {
        if (description == null || description.trim().isEmpty())
            throw new NullPointerException("Description should not be empty");
        boolean error = false;

        int howmany = -1;   // ignore current
        String myClassName = TimeUtils.class.getName();
        String myMethod = "timeImpl";
        for (StackTraceElement ste : Thread.currentThread().getStackTrace())
            if (ste.getClassName().equals(myClassName) && ste.getMethodName().equals(myMethod))
                howmany++;
        description = TextUtils.repeatString("  ", howmany) + description.trim();

        Log.info(description);
        long nano = System.nanoTime();
        try {
            R result = r.run();
            nano = System.nanoTime() - nano;
            return result;
        } catch (Throwable th) {
            nano = System.nanoTime() - nano;
            error = true;
            BaseUtils.throwException(th);
            return null;
        } finally {
            log(nano, description, error);
        }
    }

    private static void log(long nano, String description, boolean error) {
        if (error)
            Log.error(description + ": Error found");
        Log.debug(description + ": time elapsed " + (nano / 1_000_000_000.d) + "s");
    }

    public interface Procedure {

        void run() throws Throwable;
    }

    public interface ProcedureR<R> {

        R run() throws Throwable;
    }

}
