package edu.wctc;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class DiceGame {
    private  final List<Player> players = new ArrayList<>();
    private final List<Die> dice = new ArrayList<>();
    private final int maxRolls;
    private Player currentPlayer;


    public DiceGame(int countPlayers, int countDice, int maxRolls){
        if(countPlayers < 2){
            throw new IllegalStateException();
        }
        else{
            for (int i = 0; i < countPlayers; i++){
                Player player = new Player();
                players.add(player);
            }
            for (int i = 0; i < countDice; i++){
                Die die = new Die(6);
                dice.add(die);
            }
            this.maxRolls = maxRolls;
        }

    }

    public boolean allDiceHeld(){
        List<Die> heldDice = dice.stream().filter(held -> held.isBeingHeld()==true).collect(Collectors.toList());

        if(heldDice.size() == 5){
            return true;
        }
        else return false;
    }

    public boolean autoHold(int faceValue){
        if(isHoldingDie(faceValue)){
            return true;
        }
        for (Die die : dice) {
            if(die.getFaceValue() == faceValue){
                die.holdDie();
                return true;
            }
        }

        return false;
    }

    public boolean currentPlayerCanRoll(){
        if(currentPlayer.getRollsUsed() != maxRolls){
            return true;
        }
        else return false;
    }

    public int getCurrentPlayerNumber(){
        return currentPlayer.getPlayerNumber();
    }

    public int getCurrentPlayerScore(){
        return currentPlayer.getScore();
    }

    public String getDiceResults(){
        String allHeldNums = "";
        for(Die die : dice){
            allHeldNums = allHeldNums + die.toString() + "\n";
        }
        return allHeldNums;
    }

    public String getFinalWinner(){
        Player winner = players.stream().max(Comparator.comparing(Player::getWins)).orElseThrow();
        return winner.toString();
    }

    public String getGameResults(){
        List<Player> endPlayers = players.stream()
                .sorted(Comparator.comparing(Player::getScore))
                .collect(Collectors.toList());
        for (Player player : endPlayers) {
            if(player.getPlayerNumber() != endPlayers.get(endPlayers.size()-1).getPlayerNumber()){
                player.addLoss();
            }

        }
        endPlayers.get(endPlayers.size()-1).addWin();


        String allPlayerScores = "";
        for(Player player : endPlayers){
            allPlayerScores ="\n" + player.toString() + "" + allPlayerScores;
        }

        return allPlayerScores;
    }

    private boolean isHoldingDie(int faceValue){
        List <Die> matchingDice = dice.stream()
                .filter(match -> match.isBeingHeld() == true)
                .collect(Collectors.toList());
        for(Die die : matchingDice){
            if(die.getFaceValue()==faceValue){
                return true;
            }
        }
        return false;
    }

    public boolean nextPlayer(){
        if(!(currentPlayer.getPlayerNumber() >= players.size())){
            currentPlayer = players.get(currentPlayer.getPlayerNumber());
            return true;
        }
        else return false;
    }

    public void playerHold(char dieNumber){
        List<Die> die = dice.stream().filter(num -> num.getDieNum() == dieNumber).collect(Collectors.toList());
        die.get(0).holdDie();
    }

    public void resetDice(){
        dice.stream().forEach(die -> die.resetDie());

    }

    public void resetPlayers(){
        players.stream().forEach(player -> player.resetPlayer());
    }

    public void rollDice(){
        currentPlayer.roll();
        dice.forEach(die -> die.rollDie());
    }

    public void scoreCurrentPlayer(){
        int playerScore = 0;
        List<Die> heldDice = dice.stream().filter(held -> held.isBeingHeld()==true).collect(Collectors.toList());
        if(!(heldDice.size() >=3)){
            currentPlayer.setScore(0);
        }
        else{
            for (Die die : dice) {
                playerScore = playerScore + die.getFaceValue();
            }
            currentPlayer.setScore(playerScore-15);
        }





    }

    public void startNewGame(){
        resetPlayers();
        currentPlayer = players.get(0);

    }


}
