package com.halfheartstudios.network;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import static java.security.AccessController.getContext;

/**
 * Created by Jacob on 2017-08-21.
 */

public class AgentManager {
    public ArrayList<Agent> agents;

    public AgentManager() {
        this.agents = new ArrayList<>();
    }

    public HashMap<String, Agent> getAgents() {
        HashMap<String, Agent> map = new HashMap<>();

        for(int i = 0; i < this.agents.size(); i++) {
            Agent a = this.agents.get(i);

            map.put(a.getIdentifier(), a);
        }

        return map;
    }

    public void createAgentsFromContacts(Context context) {
        try {
            final String SORT_ORDER = ContactsContract.Data.DISPLAY_NAME;
            Cursor phones = context.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, SORT_ORDER);
            while (phones.moveToNext()) {
                Agent agent = new Agent();

                agent.setIndex(agents.size());
                agent.setIdentifier(phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)));

                /*Check if name already exists
                if(agents.containsKey(agent.getIdentifier())) {
                    agent = agents.get(agent.getIdentifier());
                    agents.
                }

                //contact.numbers.add(phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)));
        */
                agents.add(agent);
            }
            phones.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Map getLetteredAgentMap() {
        final HashMap<String, Agent> agents = getAgents();

        // Put contacts into lettered categories
        Map<Character, ArrayList<Agent>> map = new TreeMap<>();
        ArrayList<Agent> list;
        for(Agent agent : agents.values()) {
            String identifier = agent.getIdentifier();

            if(identifier == null) { continue; }

            Character initial = identifier.charAt(0);

            if(map.containsKey(initial)) {
                map.get(initial).add(agent);
            } else {
                list = new ArrayList<>();
                list.add(agent);
                map.put(initial, list);
            }
        }

        return map;
    }

    public void saveSharedPrefs(Context context) {
        SharedPreferences pref = context.getSharedPreferences("omega_agents", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();

        String prefix = "omega_§§";
        final HashMap<String, Agent> agentsMap = getAgents();

        for(Agent agent : agentsMap.values()) {
            Bundle b = agent.toBundle();
            Set<String> keySet = b.keySet();
            Iterator<String> it = keySet.iterator();

            while(it.hasNext()) {
                String bundleKey = it.next();
                Object o = b.get(bundleKey);
                String identifier = prefix + agent.getIdentifier() + "§" + bundleKey;

                // https://stackoverflow.com/questions/13660889/save-bundle-to-sharedpreferences

                if(o == null) {
                    editor.remove(identifier);
                } else if (o instanceof CharSequence) {
                    editor.putString(identifier, o.toString());
                }
            }

            editor.commit();

        }
    }

    public void loadSharedPrefs(Context context) {
        SharedPreferences pref = context.getSharedPreferences("omega_agents", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();

        Map<String, ?> all = pref.getAll();
        Iterator<String> it = all.keySet().iterator();

        String prefix = "omega_§§";
        Map<String, Bundle> agentsMap = new TreeMap<>();

        while(it.hasNext()) {
            String prefKey = it.next();
            if(prefKey.startsWith(prefix)) {
                // This preference is relephant
                String identifier = prefKey.substring(prefix.length()).replaceAll("§.*", "");
                String currentKey = prefKey.replaceAll(".*§", "");

                Bundle b;
                if(agentsMap.containsKey(identifier)) {
                    b = agentsMap.get(identifier);
                } else {
                    b = new Bundle();
                }

                Object o = all.get(prefKey);
                if (o == null) {
                    // ignore
                } else if (o instanceof CharSequence) {
                    b.putString(currentKey, o.toString());
                }

                agentsMap.put(identifier, b);
            }
        }

        // The agent bundles are all built, now convert to objects and save

        for (Bundle b : agentsMap.values()) {
            agents.add(Agent.fromBundle(b));
        }

        int size = agents.size();
    }
}
