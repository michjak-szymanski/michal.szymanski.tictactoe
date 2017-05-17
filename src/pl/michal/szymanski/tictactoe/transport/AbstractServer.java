/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.michal.szymanski.tictactoe.transport;

import java.util.concurrent.ConcurrentHashMap;
import pl.michal.szymanski.tictactoe.core.MultiplayerPlay;

/**
 *
 * @author Michał Szymański, kontakt: michal.szymanski.aajar@gmail.com
 */
public interface AbstractServer extends MultiplayerParticipant {

    public static ConcurrentHashMap<String, MultiplayerPlay> activeSessions = new ConcurrentHashMap();

    default void createNewPlay(){

    }

    default void addWatcher(){

    }

}