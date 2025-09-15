/**
 * Class represents solar panels, street map, and
 * an array of parking lot projects.
 *
 * @author Jessica De Brito
 * @author Kal Pandit
 */
public class SolarPanels {
    
    private Panel[][] panels;
    private String[][] streetMap;
    private ParkingLot[] lots;

    /**
     * Default constructor: initializes empty panels and objects.
     */
    public SolarPanels() {
        panels = null;
        streetMap = null;
        lots = null;
        StdRandom.setSeed(2023);
    }

    /**
     * Updates the instance variable streetMap to be an l x w
     * array of Strings. Reads each label from input file in parameters.
     * 
     * @param streetMapFile the input file to read from
     */
    public void setupStreetMap(String streetMapFile) {
        // WRITE YOUR CODE HERE
            StdIn.setFile(streetMapFile);
            
            int length = StdIn.readInt();
            int width = StdIn.readInt();
            
            streetMap = new String[length][width];
            
            for (int i = 0; i < length; i++) {
                for (int j = 0; j < width; j++) {
                    streetMap[i][j] = StdIn.readString();
                }
            }
        }
        
    

    /**
     * Adds parking lot information to an array of parking lots.
     * Updates the instance variable lots to store these parking lots.
     * 
     * @param parkingLotFile the lot input file to read
     */
    public void setupParkingLots(String parkingLotFile) {
        // WRITE YOUR CODE HERE
        StdIn.setFile(parkingLotFile);
        int n = StdIn.readInt();
        lots = new ParkingLot[n];

        for (int i = 0; i < n; i++) {
            String name = StdIn.readString();
            int maxPanels = StdIn.readInt();
            double budget = StdIn.readDouble();
            int energyCapacity = StdIn.readInt();
            double panelEfficiency = StdIn.readDouble();

            lots[i] = new ParkingLot(name, maxPanels, budget, energyCapacity, panelEfficiency);

        }
    }
    /**
     * Insert panels on each lot as much as space and budget allows.
     * Updates the instance variable panels to be a 2D array parallel to
     * streetMap, storing panels placed.
     * 
     * Panels have a 95% chance of working. Use StdRandom.uniform(); if
     * the resulting value is < 0.95 the panel works.
     * 
     * @param costPerPanel the fixed cost per panel, as a double
     */
    public void insertPanels(double costPerPanel) {
        // WRITE YOUR CODE HERE
        panels = new Panel[streetMap.length][streetMap[0].length];

        for(int i = 0; i < lots.length; i++){
            ParkingLot lot = lots[i];
            double currentBudget = lot.getBudget();
            int currentPanels = 0;

            for (int r = 0; r < streetMap.length; r++){
                for (int c = 0; c < streetMap[r].length; c++){
                    if (streetMap[r][c].equals(lot.getLotName())){
                        if (currentBudget >= costPerPanel && currentPanels < lot.getMaxPanels()){
                            boolean works = StdRandom.uniform() < 0.95;
                            Panel newPanel = new Panel(lot.getPanelEfficiency(), lot.getEnergyCapacity(), works);

                            panels[r][c] = newPanel;
                            currentBudget -= costPerPanel;
                            currentPanels++;
                        }
                    }
                }
            }
        }


    }

    /**
     * Given a temperature and coefficient, update panels' actual efficiency
     * values. Panels are most optimal at 77 degrees F.
     * 
     * Panels perform worse in hotter environments and better in colder ones.
     * worse = efficiency loss, better = efficiency gain.
     * 
     * Coefficients are usually negative to represent energy loss.
     * 
     * @param temperature the current temperature, in degrees F
     * @param coefficient the coefficient to use
     */
    public void updateActualEfficiency(int temperature, double coefficient) {
        // WRITE YOUR CODE HERE
        double efficiencyChange = coefficient * (77 - temperature);

        for (int r = 0; r < panels.length; r++){
            for (int c = 0; c < panels[r].length; c++){
                Panel panel = panels[r][c];
                if (panel != null){
                    double actualEfficiency = panel.getRatedEfficiency() + efficiencyChange;
                    panel.setActualEfficiency(actualEfficiency);
                }
            }
        }
    }

    /**
     * For each WORKING panel, update the electricity generated for 4 hours 
     * of sunlight as follows:
     * 
     * (actual efficiency / 100) * 1500 * 4
     * 
     * RUN updateActualEfficiency BEFORE running this method.
     */
    public void updateElectricityGenerated() {
        // WRITE YOUR CODE HERE
        double panelArea = 1.5; //in m^2
        double solarRadiation = 1000;  //in watts/m^2
        int avgSunlight = 4; //NJ avg sunlight in hours

        for (int r = 0; r < panels.length; r++){
            for (int c = 0; c < panels[r].length; c++){
                Panel panel = panels[r][c];
                if (panel != null && panel.isWorking()){
                    double powerOutput = (panel.getActualEfficiency() / 100) * 1500;
                    int dailyWattHours = (int) (powerOutput * avgSunlight);
                    panel.setElectricityGenerated(dailyWattHours);

                }
            }
        }
    }
    /**
     * Count the number of working panels in a parking lot.
     * 
     * @param parkingLot the parking lot name
     * @return the number of working panels
     */
    public int countWorkingPanels(String parkingLot) {
        // WRITE YOUR CODE HERE
        int count = 0;

        for (int r = 0; r < panels.length; r++){
            for (int c = 0; c < panels[r].length; c++){
                Panel panel = panels[r][c];

                if (panel != null && streetMap[r][c].equals(parkingLot) && panel.isWorking()){
                    count++;
                }
            }
        }
        return count;
    }

    /**
     * Find the broken panels in the map and repair them.
     * @return the count of working panels in total, after repair
     */
    public int updateWorkingPanels() {
        // WRITE YOUR CODE HERE
        int working = 0;

        for (int r = 0; r < panels.length; r++){
            for (int c = 0; c < panels[r].length; c++){
                Panel panel = panels[r][c];

                if (panel != null){ //checks for panel
                    if (!panel.isWorking()){ //check if that panel isn't working
                        panel.setIsWorking(true); //fixes broken panels
                    }
                    working++;
                }
            }
        }
        return working;
    }

    /**
     * Calculate Rutgers' savings on energy by using
     * these solar panels.
     * 
     * ASSUME:
     * - Multiply total electricity generated by 0.001 to convert to KwH.
     * - There are 365 days in a year.
     * 
     * RUN electricityGenerated before running this method.
     */
    public double calculateSavings() {
        // WRITE YOUR CODE HERE
        double totalElectricity = 0.0;

        for (int r = 0; r < panels.length; r++){
            for (int c = 0; c < panels[r].length; c++){
                Panel panel = panels[r][c];

                if (panel != null && panel.isWorking()){
                    totalElectricity += panel.getElectricityGenerated();
                }
            }
        }
        double dailyElectricity = totalElectricity * 0.001;
        double yearlyElectricity = dailyElectricity * 365;
        double RutgersNeeds = yearlyElectricity / 4270000.0;
        double moneySaved = RutgersNeeds * 60000000.0;

        return moneySaved;
    }

    /*
     * Getter and Setter methods
     */
    public Panel[][] getPanels() {
        // DO NOT TOUCH THIS METHOD
        return this.panels;
    }

    public void setPanels(Panel[][] panels) {
        // DO NOT TOUCH THIS METHOD
        this.panels = panels;
    }

    public String[][] getStreetMap() {
        // DO NOT TOUCH THIS METHOD
        return this.streetMap;
    }

    public void setStreetMap(String[][] streetMap) {
        // DO NOT TOUCH THIS METHOD
        this.streetMap = streetMap;
    }

    public ParkingLot[] getLots() {
        // DO NOT TOUCH THIS METHOD
        return this.lots;
    }

    public void setLots(ParkingLot[] lots) {
        // DO NOT TOUCH THIS METHOD
        this.lots = lots;
    }
}
