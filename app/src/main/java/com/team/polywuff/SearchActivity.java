package com.team.polywuff;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.sendbird.android.GroupChannel;
import com.sendbird.android.SendBird;
import com.sendbird.android.SendBirdException;
import com.sendbird.android.User;
import com.sendbird.android.UserListQuery;
import com.team.polywuff.Messenger.GroupChannelListFragment;
import com.team.polywuff.Messenger.SelectableUserListAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chris_xtx7ln9 on 04/04/2017.
 */

public class SearchActivity extends AppCompatActivity
{
    private LinearLayoutManager searchLayoutManager;
    private RecyclerView searchRecyclerView;
    private SelectableUserListAdapter usersFound;

    private UserListQuery usersPool;

    private Button userChatSelectBtn;
    private String chatId;

    private List<String> chosenUsers;

    public static SearchActivity newInstance() {
        SearchActivity fragment = new SearchActivity();

        return fragment;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_invite_member);

        chosenUsers = new ArrayList<>();

        searchRecyclerView = (RecyclerView) findViewById(R.id.recycler_invite_member);
        usersFound = new SelectableUserListAdapter(this);
        usersFound.setItemCheckedChangeListener(new SelectableUserListAdapter.OnItemCheckedChangeListener() {
            @Override
            public void OnItemChecked(User user, boolean checked) {
                if (checked) {
                    chosenUsers.add((user.getUserId()));
                } else {
                    chosenUsers.remove(user.getUserId());
                }

                // If no users are selected, disable the invite button.
                if (chosenUsers.size() > 0) {
                    userChatSelectBtn.setEnabled(true);
                } else {
                    userChatSelectBtn.setEnabled(false);
                }
            }
        });

        userChatSelectBtn = (Button) findViewById(R.id.button_invite_member);
        userChatSelectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (chosenUsers.size() > 0) {
                    inviteSelectedMembersWithUserIds();
                }
            }
        });
        userChatSelectBtn.setEnabled(false);

        setUpRecyclerView();

        loadInitialUserList(15);
    }

    private void setUpRecyclerView() {
        searchLayoutManager = new LinearLayoutManager(this);
        searchRecyclerView.setLayoutManager(searchLayoutManager);
        searchRecyclerView.setAdapter(usersFound);
        searchRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        searchRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (searchLayoutManager.findLastVisibleItemPosition() == usersFound.getItemCount() - 1) {
                    loadNextUserList(10);
                }
            }
        });
    }

    private void loadInitialUserList(int size) {
        usersPool = SendBird.createUserListQuery();

        usersPool.setLimit(size);
        usersPool.next(new UserListQuery.UserListQueryResultHandler() {
            @Override
            public void onResult(List<User> list, SendBirdException e) {
                if (e != null) {
                    // Error!
                    return;
                }

                usersFound.setUserList(list);
            }
        });
    }

    private void loadNextUserList(int size) {
        usersPool.setLimit(size);

        usersPool.next(new UserListQuery.UserListQueryResultHandler() {
            @Override
            public void onResult(List<User> list, SendBirdException e) {
                if (e != null) {
                    // Error!
                    return;
                }

                for (User user : list) {
                    usersFound.addLast(user);
                }
            }
        });
    }

    private void inviteSelectedMembersWithUserIds() {

        // Get channel instance from URL first.
        GroupChannel.getChannel(chatId, new GroupChannel.GroupChannelGetHandler() {
            @Override
            public void onResult(GroupChannel groupChannel, SendBirdException e) {
                if (e != null) {
                    // Error!
                    return;
                }

                // Then invite the selected members to the channel.
                groupChannel.inviteWithUserIds(chosenUsers, new GroupChannel.GroupChannelInviteHandler() {
                    @Override
                    public void onResult(SendBirdException e) {
                        if (e != null) {
                            // Error!
                            return;
                        }

                        finish();
                    }
                });
            }
        });
    }
}