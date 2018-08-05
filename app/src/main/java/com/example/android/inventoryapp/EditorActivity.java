package com.example.android.inventoryapp;

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

import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.android.inventoryapp.data.BookContract.BookEntry;

/**
 * Allows user to create a new book or edit an existing one.
 */
public class EditorActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor> {

    /**
     * Identifier for the book data loader
     */
    private static final int EXISTING_BOOK_LOADER = 0;

    /**
     * Content URI for the existing book (null if it's a new book)
     */
    private Uri mCurrentBookUri;

    /**
     * EditText field to enter the book's name
     */
    private EditText mNameEditText;

    /**
     * EditText field to enter the book's price
     */
    private EditText mPriceEditText;

    /**
     * EditText field to enter the book's supplier
     */
    private EditText mSupplierEditText;

    /**
     * EditText field to enter the book's supplier phone number
     */
    private EditText mPhoneEditText;

    /**
     * EditText field to enter the book's quantity
     */
    private EditText mQuantityEditText;

    int quantity = 0;

    /** EditText field to enter the quantity
     private EditText mQuantity;

     */

    /**
     * EditText field to enter the Book' quantity

     private Spinner mQuantitySpinner;
     */
    /**
     * Quantity of the book. The possible valid values are in the BookContract.java file:
     * {@link BookEntry#QUANTITY_UNKNOWN}, {@link BookEntry#QUANTITY_IN_STOCK}, or
     * {@link BookEntry#QUANTITY_OUT_OF_STOCK}.
     */
    // private int mQuantity = BookEntry.QUANTITY_UNKNOWN;


    /**
     * Boolean flag that keeps track of whether the book has been edited (true) or not (false)
     */
    private boolean mBookHasChanged = false;

    /**
     * OnTouchListener that listens for any user touches on a View, implying that they are modifying
     * the view, and we change the mBookHasChanged boolean to true.
     */
    private View.OnTouchListener mTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            mBookHasChanged = true;
            return false;
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        // Examine the intent that was used to launch this activity,
        // in order to figure out if we're creating a new book or editing an existing one.
        Intent intent = getIntent();
        mCurrentBookUri = intent.getData();

        // If the intent DOES NOT contain a book content URI, then we know that we are
        // creating a new book.
        if (mCurrentBookUri == null) {
            // This is a new book, so change the app bar to say "Add a Book"
            setTitle(getString(R.string.editor_activity_title_new_book));

            // Invalidate the options menu, so the "Delete" menu option can be hidden.
            // (It doesn't make sense to delete a book that hasn't been created yet.)
            invalidateOptionsMenu();
        } else {
            // Otherwise this is an existing book, so change app bar to say "Edit Book"
            setTitle(getString(R.string.editor_activity_title_edit_book));

            // Initialize a loader to read the book data from the database
            // and display the current values in the editor
            getLoaderManager().initLoader(EXISTING_BOOK_LOADER, null, this);
        }

        // Find all relevant views that we will need to read user input from
        mNameEditText = (EditText) findViewById(R.id.edit_book_name);
        mPriceEditText = (EditText) findViewById(R.id.edit_book_price);
        mSupplierEditText = (EditText) findViewById(R.id.edit_book_supplier);
        mPhoneEditText = (EditText) findViewById(R.id.edit_book_phone);
        mQuantityEditText = (EditText) findViewById(R.id.input_quantity);


        //mQuantitySpinner = (Spinner) findViewById(R.id.spinner_quantity);


        // Setup OnTouchListeners on all the input fields, so we can determine if the user
        // has touched or modified them. This will let us know if there are unsaved changes
        // or not, if the user tries to leave the editor without saving.
        mNameEditText.setOnTouchListener(mTouchListener);
        mPriceEditText.setOnTouchListener(mTouchListener);
        mSupplierEditText.setOnTouchListener(mTouchListener);
        mPhoneEditText.setOnTouchListener(mTouchListener);
        mQuantityEditText.setOnTouchListener(mTouchListener);

        //mQuantitySpinner.setOnTouchListener(mTouchListener);
        //setupSpinner();
    }


    public void increment(View view) {
        // Read from input fields
        // Use trim to eliminate leading or trailing white space
        String quantityString = mQuantityEditText.getText().toString().trim();
        if (TextUtils.isEmpty(quantityString)) {
            Toast.makeText(EditorActivity.this, getString(R.string.quantity_empty), Toast.LENGTH_SHORT).show();
        } else {
            quantity = Integer.parseInt(quantityString);
            quantity++;
            mQuantityEditText.setText(String.valueOf(quantity));

        }

    }

    public void decrement(View view) {
        // Read from input fields
        // Use trim to eliminate leading or trailing white space
        String quantityString = mQuantityEditText.getText().toString().trim();
        if (TextUtils.isEmpty(quantityString)) {
            Toast.makeText(EditorActivity.this, getString(R.string.quantity_empty), Toast.LENGTH_SHORT).show();
        } else {
            quantity = Integer.parseInt(quantityString);
            do {
                quantity--;
                mQuantityEditText.setText(String.valueOf(quantity));
                Toast.makeText(EditorActivity.this, getString(R.string.quantity_no_negative), Toast.LENGTH_SHORT).show();

            }while (quantity < 0);
        }

    }

    /*Phone to call the supplier*/
    public void phone(View view) {
        // Read from input fields
        // Use trim to eliminate leading or trailing white space
        String phoneString = mPhoneEditText.getText().toString().trim();
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + phoneString));
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }


    /**
     * Setup the dropdown spinner that allows the user to select the quantity of the book.

     private void setupSpinner() {
     // Create adapter for spinner. The list options are from the String array it will use
     // the spinner will use the default layout
     ArrayAdapter quantitySpinnerAdapter = ArrayAdapter.createFromResource(this,
     R.array.array_quantity_options, android.R.layout.simple_spinner_item);

     // Specify dropdown layout style - simple list view with 1 item per line
     quantitySpinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

     // Apply the adapter to the spinner
     mQuantitySpinner.setAdapter(quantitySpinnerAdapter);

     // Set the integer mSelected to the constant values
     mQuantitySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
    @Override public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
    String selection = (String) parent.getItemAtPosition(position);
    if (!TextUtils.isEmpty(selection)) {
    if (selection.equals(getString(R.string.quantity_in_stock))) {
    mQuantity = BookEntry.QUANTITY_IN_STOCK;
    } else if (selection.equals(getString(R.string.quantity_out_of_stock))) {
    mQuantity = BookEntry.QUANTITY_OUT_OF_STOCK;
    } else {
    mQuantity = BookEntry.QUANTITY_UNKNOWN;
    }
    }
    }

    // Because AdapterView is an abstract class, onNothingSelected must be defined
    @Override public void onNothingSelected(AdapterView<?> parent) {
    mQuantity = BookEntry.QUANTITY_UNKNOWN;
    }
    });
     }
     */
    /**
     * Get user input from editor and save book into database.
     */
    private void saveBook() {
        // Read from input fields
        // Use trim to eliminate leading or trailing white space
        String nameString = mNameEditText.getText().toString().trim();
        String priceString = mPriceEditText.getText().toString().trim();
        String supplierString = mSupplierEditText.getText().toString().trim();
        String quantityString = mQuantityEditText.getText().toString().trim();
        String phoneString = mPhoneEditText.getText().toString().trim();

        // Check if this is supposed to be a new book
        // and check if all the fields in the editor are blank
        if (mCurrentBookUri == null && TextUtils.isEmpty(nameString) && TextUtils.isEmpty(priceString) &&
                TextUtils.isEmpty(supplierString) && TextUtils.isEmpty(phoneString) && TextUtils.isEmpty(quantityString)) {
            // Since no fields were modified, we can return early without creating a new book.
            // No need to create ContentValues and no need to do any ContentProvider operations.
            return;
        }
        ContentValues values = new ContentValues();

        if (TextUtils.isEmpty(nameString)) {
            Toast.makeText(this, getString(R.string.enter_book_name),Toast.LENGTH_SHORT).show();
            return;

        }
        else {
            values.put(BookEntry.COLUMN_BOOK_NAME, nameString);
        }

        if (TextUtils.isEmpty(quantityString)) {
            Toast.makeText(this, getString(R.string.enter_book_quantity),Toast.LENGTH_SHORT).show();
            return;

        }
        else {
            values.put(BookEntry.COLUMN_BOOK_QUANTITY, quantityString);
        }

        if (TextUtils.isEmpty(priceString)) {
            Toast.makeText(this, getString(R.string.enter_book_price),Toast.LENGTH_SHORT).show();
            return;

        }
        else {
            values.put(BookEntry.COLUMN_BOOK_PRICE, priceString);
        }

        if (TextUtils.isEmpty(supplierString)) {
            Toast.makeText(this, getString(R.string.enter_book_supplier),Toast.LENGTH_SHORT).show();
            return;
        }

        else {
            values.put(BookEntry.COLUMN_BOOK_PRICE, supplierString);
        }

        if (TextUtils.isEmpty(phoneString)) {
            Toast.makeText(this, getString(R.string.enter_book_phone),Toast.LENGTH_SHORT).show();
            return;

        }
        else {
            values.put(BookEntry.COLUMN_BOOK_PRICE, phoneString);
        }


        // If the weight is not provided by the user, don't try to parse the string into an
        // integer value. Use 0 by default.
       /*  int price = 0;
        if (!TextUtils.isEmpty(priceString)) {
            price = Integer.parseInt(priceString);
        }
        values.put(BookEntry.COLUMN_BOOK_PRICE, price);*/

        // Determine if this is a new or existing book by checking if mCurrentBookUri is null or not
        if (mCurrentBookUri == null) {
            // This is a NEW book, so insert a new book into the provider,
            // returning the content URI for the new book.
            Uri newUri = getContentResolver().insert(BookEntry.CONTENT_URI, values);

            // Show a toast message depending on whether or not the insertion was successful.
            if (newUri == null) {
                // If the new content URI is null, then there was an error with insertion.
                Toast.makeText(this, getString(R.string.editor_insert_book_failed),
                        Toast.LENGTH_SHORT).show();
            } else {
                // Otherwise, the insertion was successful and we can display a toast.
                Toast.makeText(this, getString(R.string.editor_insert_book_successful),
                        Toast.LENGTH_SHORT).show();
            }
        } else {
            // Otherwise this is an EXISTING book, so update the book with content URI: mCurrentBookUri
            // and pass in the new ContentValues. Pass in null for the selection and selection args
            // because mCurrentBookUri will already identify the correct row in the database that
            // we want to modify.
            int rowsAffected = getContentResolver().update(mCurrentBookUri, values, null, null);

            // Show a toast message depending on whether or not the update was successful.
            if (rowsAffected == 0) {
                // If no rows were affected, then there was an error with the update.
                Toast.makeText(this, getString(R.string.editor_update_book_failed),
                        Toast.LENGTH_SHORT).show();
            } else {
                // Otherwise, the update was successful and we can display a toast.
                Toast.makeText(this, getString(R.string.editor_update_book_successful),
                        Toast.LENGTH_SHORT).show();
            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_editor.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_editor, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Save" menu option
            case R.id.action_save:
                // Save book to database
                saveBook();
                // Exit activity
                finish();
                return true;
            // Respond to a click on the "Delete" menu option
            case R.id.action_delete:
                // Pop up confirmation dialog for deletion
                showDeleteConfirmationDialog();
                return true;
            // Respond to a click on the "Up" arrow button in the app bar
            case android.R.id.home:
                // If the book hasn't changed, continue with navigating up to parent activity
                // which is the {@link CatalogActivity}.
                if (!mBookHasChanged) {
                    NavUtils.navigateUpFromSameTask(EditorActivity.this);
                    return true;
                }

                // Otherwise if there are unsaved changes, setup a dialog to warn the user.
                // Create a click listener to handle the user confirming that
                // changes should be discarded.
                DialogInterface.OnClickListener discardButtonClickListener =
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                // User clicked "Discard" button, navigate to parent activity.
                                NavUtils.navigateUpFromSameTask(EditorActivity.this);
                            }
                        };

                // Show a dialog that notifies the user they have unsaved changes
                showUnsavedChangesDialog(discardButtonClickListener);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    /**
     * This method is called when the back button is pressed.
     */
    @Override
    public void onBackPressed() {
        // If the book hasn't changed, continue with handling back button press
        if (!mBookHasChanged) {
            super.onBackPressed();
            return;
        }

        // Otherwise if there are unsaved changes, setup a dialog to warn the user.
        // Create a click listener to handle the user confirming that changes should be discarded.
        DialogInterface.OnClickListener discardButtonClickListener =
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // User clicked "Discard" button, close the current activity.
                        finish();
                    }
                };

        // Show dialog that there are unsaved changes
        showUnsavedChangesDialog(discardButtonClickListener);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        // Since the editor shows all book attributes, define a projection that contains
        // all columns from the bok table
        String[] projection = {
                BookEntry._ID,
                BookEntry.COLUMN_BOOK_NAME,
                BookEntry.COLUMN_BOOK_PRICE,
                BookEntry.COLUMN_BOOK_QUANTITY,
                BookEntry.COLUMN_BOOK_SUPPLIER,
                BookEntry.COLUMN_BOOK_PHONE};

        // This loader will execute the ContentProvider's query method on a background thread
        return new CursorLoader(this,   // Parent activity context
                mCurrentBookUri,         // Query the content URI for the current book
                projection,             // Columns to include in the resulting Cursor
                null,                   // No selection clause
                null,                   // No selection arguments
                null);                  // Default sort order
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        // Bail early if the cursor is null or there is less than 1 row in the cursor
        if (cursor == null || cursor.getCount() < 1) {
            return;
        }

        // Proceed with moving to the first row of the cursor and reading data from it
        // (This should be the only row in the cursor)
        if (cursor.moveToFirst()) {
            // Find the columns of book attributes that we're interested in
            int nameColumnIndex = cursor.getColumnIndex(BookEntry.COLUMN_BOOK_NAME);
            int priceColumnIndex = cursor.getColumnIndex(BookEntry.COLUMN_BOOK_PRICE);
            int quantityColumnIndex = cursor.getColumnIndex(BookEntry.COLUMN_BOOK_QUANTITY);
            int supplierColumnIndex = cursor.getColumnIndex(BookEntry.COLUMN_BOOK_SUPPLIER);
            int phoneColumnIndex = cursor.getColumnIndex(BookEntry.COLUMN_BOOK_PHONE);

            // Extract out the value from the Cursor for the given column index
            String name = cursor.getString(nameColumnIndex);
            int price = cursor.getInt(priceColumnIndex);
            int quantity = cursor.getInt(quantityColumnIndex);
            String supplier = cursor.getString(supplierColumnIndex);
            int phone = cursor.getInt(phoneColumnIndex);

            // Update the views on the screen with the values from the database
            mNameEditText.setText(name);
            mPriceEditText.setText(Integer.toString(price));
            mSupplierEditText.setText(supplier);
            mQuantityEditText.setText(Integer.toString(quantity));
            mPhoneEditText.setText(Integer.toString(phone));

      /*      // Quantity is a dropdown spinner, so map the constant value from the database
            // into one of the dropdown options (0 is Unknown, 1 is Male, 2 is Female).
            // Then call setSelection() so that option is displayed on screen as the current selection.
            switch (quantity) {
                case BookEntry.QUANTITY_IN_STOCK:
                    mQuantitySpinner.setSelection(1);
                    break;
                case BookEntry.QUANTITY_OUT_OF_STOCK:
                    mQuantitySpinner.setSelection(2);
                    break;
                default:
                    mQuantitySpinner.setSelection(0);
                    break;
                    */
        }
    }



    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        // If the loader is invalidated, clear out all the data from the input fields.
        mNameEditText.setText("");
        mPriceEditText.setText("");
        mQuantityEditText.setText("");
        mSupplierEditText.setText("");
        mPhoneEditText.setText("");
        //mQuantitySpinner.setSelection(0); // Select "Unknown" quantity
    }

    /**
     * Show a dialog that warns the user there are unsaved changes that will be lost
     * if they continue leaving the editor.
     *
     * @param discardButtonClickListener is the click listener for what to do when
     *                                   the user confirms they want to discard their changes
     */
    private void showUnsavedChangesDialog(
            DialogInterface.OnClickListener discardButtonClickListener) {
        // Create an AlertDialog.Builder and set the message, and click listeners
        // for the postivie and negative buttons on the dialog.
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.unsaved_changes_dialog_msg);
        builder.setPositiveButton(R.string.discard, discardButtonClickListener);
        builder.setNegativeButton(R.string.keep_editing, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Keep editing" button, so dismiss the dialog
                // and continue editing the book.
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        // Create and show the AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    /**
     * Prompt the user to confirm that they want to delete this book.
     */
    private void showDeleteConfirmationDialog() {
        // Create an AlertDialog.Builder and set the message, and click listeners
        // for the postivie and negative buttons on the dialog.
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.delete_dialog_msg);
        builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Delete" button, so delete the book.
                deleteBook();
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Cancel" button, so dismiss the dialog
                // and continue editing the book.
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        // Create and show the AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    /**
     * Perform the deletion of the book in the database.
     */
    private void deleteBook() {
        // Only perform the delete if this is an existing book.
        if (mCurrentBookUri != null) {
            // Call the ContentResolver to delete the book at the given content URI.
            // Pass in null for the selection and selection args because the mCurrentBookUri
            // content URI already identifies the book that we want.
            int rowsDeleted = getContentResolver().delete(mCurrentBookUri, null, null);

            // Show a toast message depending on whether or not the delete was successful.
            if (rowsDeleted == 0) {
                // If no rows were deleted, then there was an error with the delete.
                Toast.makeText(this, getString(R.string.editor_delete_book_failed),
                        Toast.LENGTH_SHORT).show();
            } else {
                // Otherwise, the delete was successful and we can display a toast.
                Toast.makeText(this, getString(R.string.editor_delete_book_successful),
                        Toast.LENGTH_SHORT).show();
            }
        }

        // Close the activity
        finish();
    }
}

