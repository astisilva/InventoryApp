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

import android.provider.BaseColumns;

/**
 * API Contract for the Books app.
 */
public final class BookContract {

    // To prevent someone from accidentally instantiating the contract class,
    // give it an empty constructor.
    private BookContract() {}

    /**
     * Inner class that defines constant values for the Books database table.
     * Each entry in the table represents a single Book.
     */
    public static final class BookEntry implements BaseColumns {

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
    }

}

