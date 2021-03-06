package com.xiangde.dy.imchat.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.xiangde.mychat_tencent.R;
import com.xiangde.dy.imchat.adapters.ExpandGroupListAdapter;
import com.xiangde.dy.imchat.model.FriendProfile;
import com.xiangde.dy.imchat.model.FriendshipInfo;
import com.xiangde.dy.imchat.ui.customview.TemplateTitle;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ChooseFriendActivity extends Activity {


    private ExpandGroupListAdapter mGroupListAdapter;
    private ExpandableListView mGroupListView;
    private List<FriendProfile> selectList = new ArrayList<FriendProfile>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_friend);
        TemplateTitle title = (TemplateTitle) findViewById(R.id.chooseTitle);
        title.setMoreTextAction(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectList.size() == 0){
                    Toast.makeText(ChooseFriendActivity.this, getString(R.string.choose_need_one), Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent intent = new Intent();
                intent.putStringArrayListExtra("select", getSelectIds());
                setResult(RESULT_OK, intent);
                finish();
            }
        });
        final Map<String, List<FriendProfile>> friends = FriendshipInfo.getInstance().getFriends();
        mGroupListView = (ExpandableListView) findViewById(R.id.groupList);
        mGroupListAdapter = new ExpandGroupListAdapter(this, FriendshipInfo.getInstance().getGroups(), friends, true);
        mGroupListView.setAdapter(mGroupListAdapter);
        mGroupListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view, int groupPosition, int childPosition, long l) {
                onSelect(friends.get(FriendshipInfo.getInstance().getGroups().get(groupPosition)).get(childPosition));
                mGroupListAdapter.notifyDataSetChanged();
                return false;
            }
        });
        mGroupListAdapter.notifyDataSetChanged();
    }


    @Override
    public void onDestroy(){
        super.onDestroy();
        for (FriendProfile item : selectList){
            item.setIsSelected(false);
        }
    }

    private void onSelect(FriendProfile profile){
        if (!profile.isSelected()){
            selectList.add(profile);
        }else{
            selectList.remove(profile);
        }
        profile.setIsSelected(!profile.isSelected());
    }

    private ArrayList<String> getSelectIds(){
        ArrayList<String> result = new ArrayList<String>();
        for (FriendProfile item : selectList){
            result.add(item.getIdentify());
        }
        return result;
    }
}
