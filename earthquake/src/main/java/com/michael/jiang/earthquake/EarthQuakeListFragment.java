package com.michael.jiang.earthquake;

import android.app.ListFragment;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.widget.SimpleCursorAdapter;

import com.michael.jiang.earthquake.database.EarthQuakeSQLiteHelper;
import com.michael.jiang.earthquake.provider.EarthQuakeProvider;
import com.michael.jiang.earthquake.service.EarthQuakeUpdateService;

/**
 * Earthquake的Fragment
 */
public class EarthQuakeListFragment extends ListFragment implements LoaderManager.LoaderCallbacks<Cursor>{

     SimpleCursorAdapter simpleCursorAdapter;
    private Handler handler=new Handler();

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        simpleCursorAdapter = new SimpleCursorAdapter(getActivity(), android.R.layout.simple_list_item_1, null,
                new String[]{EarthQuakeSQLiteHelper.KEY_DETAILS}, new int[]{android.R.id.text1}, 0);
        setListAdapter(simpleCursorAdapter);
         getLoaderManager().initLoader(0, null, this);
        Thread t = new Thread(new Runnable() {
            public void run() {
                refreshEarthQuakes();
            }
        });
        t.start();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection=new String[]{
                EarthQuakeSQLiteHelper.KEY_ID,
                EarthQuakeSQLiteHelper.KEY_DETAILS
        };
        String selection=EarthQuakeSQLiteHelper.KEY_MAGNITUDE+">="+((MainActivity)getActivity()).minMagnitude;
        CursorLoader cursorLoader = new CursorLoader(getActivity(), EarthQuakeProvider.CONTENT_URIS,
                projection,selection,null,null );
        return cursorLoader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        simpleCursorAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        simpleCursorAdapter.swapCursor(null);
    }

    public void refreshEarthQuakes() {
        handler.post(new Runnable() {
            @Override
            public void run() {
                getLoaderManager().restartLoader(0, null, EarthQuakeListFragment.this);
            }
        });
        getActivity().startService(new Intent(getActivity(), EarthQuakeUpdateService.class));
    }


}
