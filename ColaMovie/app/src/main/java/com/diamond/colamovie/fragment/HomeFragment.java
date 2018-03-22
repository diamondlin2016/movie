package com.diamond.colamovie.fragment;

import android.view.View;

import com.diamond.colamovie.R;
import com.diamond.colamovie.activity.SearchActivity;
import com.diamond.colamovie.activity.SearchResultActivity;
import com.diamond.colamovie.base.BaseViewPagerFragment;
import com.diamond.searrchview.SearchAdapter;
import com.diamond.searrchview.SearchHistoryTable;
import com.diamond.searrchview.SearchItem;
import com.diamond.searrchview.SearchView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Author:    Diamond_Lin
 * Version    V1.0
 * Date:      2018/1/19 下午6:51
 * Description:
 * Modification  History:
 * Date         	Author        		Version        	Description
 * -----------------------------------------------------------------------------------
 * 2018/1/19      Diamond_Lin            1.0                    1.0
 * Why & What is modified:
 */

public class HomeFragment extends BaseViewPagerFragment {

    @BindView(R.id.search_view)
    SearchView mSearchView;
    private SearchHistoryTable mMHistoryDatabase;

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_home;
    }

    @Override
    protected void setUpViewComponent() {
        mMHistoryDatabase = new SearchHistoryTable(getActivity());

        mSearchView.setVersionMargins(SearchView.VersionMargins.TOOLBAR_SMALL);
        mSearchView.setHint(R.string.search);
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                mMHistoryDatabase.addItem(new SearchItem(query));
                mSearchView.close(false);
                getSearchResult(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        mSearchView.setOnOpenCloseListener(new SearchView.OnOpenCloseListener() {
            @Override
            public boolean onOpen() {
                return true;
            }

            @Override
            public boolean onClose() {
                return true;
            }
        });

        List<SearchItem> suggestionsList = new ArrayList<>();

        SearchAdapter searchAdapter = new SearchAdapter(getActivity(), suggestionsList);
        searchAdapter.setOnSearchItemClickListener(new SearchAdapter.OnSearchItemClickListener() {
            @Override
            public void onSearchItemClick(View view, int position, String text) {
                mMHistoryDatabase.addItem(new SearchItem(text));
                mSearchView.close(false);
                getSearchResult(text);
            }
        });
        mSearchView.setAdapter(searchAdapter);

        mSearchView.showKeyboard();

    }

    @Override
    protected void onLazyLoad() {

    }

    private void getSearchResult(String text) {
        SearchResultActivity.startActivity(getActivity(), text);
    }

    @OnClick(R.id.iv_search)
    void searchMovie() {
        SearchActivity.startActivity(getActivity());
    }


    @Override
    public void onPause() {
        super.onPause();
        if (mMHistoryDatabase != null) {
            mMHistoryDatabase.close();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mMHistoryDatabase != null) {
            mMHistoryDatabase.open();
        }
    }

}
