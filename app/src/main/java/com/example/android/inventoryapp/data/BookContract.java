/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.inventoryapp.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * API Contract for the Books app.
 */
public final class BookContract {

    // To prevent someone from accidentally instantiating the contract class,
    // give it an empty constructor.
    private BookContract() {}


    /**
        * The "Content authority" is a name for the entire content provider, similar to the
          * relationship between a domain name and its website.  A convenient string to use for the
          * content authority is the package name for the app, which is guaranteed to be unique on the
          * device.
          */
    public static final String CONTENT_AUTHORITY = "com.example.android.inventoryapp";

            /**
      * Use CONTENT_AUTHORITY to create the base of all URI's which apps will use to contact
      * the content provider.
      */
            public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

            /**
      * Possible path (appended to base content URI for possible URI's)
      * For instance, content://com.example.android.inventoryapp/books/ is a valid path for
      * looking at pet data. content://com.example.android.pets/staff/ will fail,
      * as the ContentProvider hasn't been given any information on what to do with "staff".
      */
            public static final String PATH_BOOKS = "books";



    /**
     * Inner class that defines constant values for the Books database table.
     * Each entry in the table represents a single Book.
     */
    public static final class BookEntry implements BaseColumns {

        /** The content URI to access the pet data in the provider */
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_BOOKS);

        /**
         * The MIME type of the {@link #CONTENT_URI} for a list of books.
         */
        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_BOOKS;

        /**
         * The MIME type of the {@link #CONTENT_URI} for a single pet.
         */
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_BOOKS;



        /** Name of database table for books */
        public final static String TABLE_NAME = "books";

        /**
         * Unique ID number for the book (only for use in the database table).
         *
         * Type: INTEGER
         */
        public final static String _ID = BaseColumns._ID;

        /**
         * Name of the book.
         *
         * Type: TEXT
         */
        public final static String COLUMN_BOOK_NAME ="Book_name";

        /**
         * Price of the book.
         *
         * Type: INTEGER
         */
        public final static String COLUMN_BOOK_PRICE = "price";

        /**
         * Quantity of the book.
         *
         * The only possible values are {@link #QUANTITY_UNKNOWN}, {@link #QUANTITY_IN_STOCK},
         * or {@link #QUANTITY_OUT_OF_STOCK}.
         *
         * Type: INTEGER
         */
        public final static String COLUMN_BOOK_QUANTITY = "quantity";

        /**
         * Supplier of the book.
         *
         * Type: INTEGER
         */
        public final static String COLUMN_BOOK_SUPPLIER= "supplier";

        /**
         * Phone to the supplier of the book.
         *
         * Type: INTEGER
         */

        public final static String COLUMN_BOOK_PHONE= "phone";

        /**
         * Possible values for the book quantity.
         */
        public static final int QUANTITY_UNKNOWN = 0;
        public static final int QUANTITY_IN_STOCK = 1;
        public static final int QUANTITY_OUT_OF_STOCK = 2;

        /**
         * Returns whether or not the given gender is {@link #QUANTITY_UNKNOWN}, {@link #QUANTITY_IN_STOCK},
         * or {@link #QUANTITY_OUT_OF_STOCK).
         */
        public static boolean isValidQuantity(int quantity) {
            if (quantity == QUANTITY_UNKNOWN || quantity == QUANTITY_IN_STOCK|| quantity == QUANTITY_OUT_OF_STOCK) {
                return true;
            }
            return false;
        }
    }

}

