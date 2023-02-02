import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Scheduling {
    public static void main(String[] args) throws FileNotFoundException {

//        ArrayList<String> lines = new ArrayList<>();
//
//        String scheduler = "";
//        String processes = "";
//
//        scheduler = args[0];
//        processes = args[1];
//
//        File schedulerFile = new File("src//" + scheduler);
//        File processFile = new File(scheduler);
//
//        Scanner input = new Scanner(schedulerFile);
//
//        while (input.hasNextLine()) {
//            lines.add(input.nextLine());
//        }
//
//        if (lines.get(0).equals("FCFS")) {
//            FCFS sim = new FCFS();
//            input = new Scanner(processFile);
//            while (input.hasNextLine()) {
//                String[] line = input.nextLine().split(" ");
//                ArrayList<Integer> process = new ArrayList<>();
//                for (int i = 0; i < line.length; i++) {
//                    int x = Integer.parseInt(line[i]);
//                    process.add(i);
//                }
//                sim.setProcesses(process);
//            }
//            sim.run();
//            sim.printStats();
//        } else if (lines.get(0).equals("VRR")) {
//
//        } else if (lines.get(0).equals("SRT")) {
//
//        } else if (lines.get(0).equals("HRRN")) {
//
//        } else if (lines.get(0).equals("FEEDBACK")) {
//
//        }


        ArrayList<Integer> one = new ArrayList<Integer>();
        one.add(0);
        one.add(10);
        one.add(3);
        one.add(10);
        one.add(4);
        one.add(2);

        ArrayList<Integer> two = new ArrayList<Integer>();
        two.add(3);
        two.add(100);
        two.add(2);
        two.add(50);
        two.add(2);
        two.add(4);

        ArrayList<Integer> three = new ArrayList<Integer>();
        three.add(7);
        three.add(3);
        three.add(2);
        three.add(2);
        three.add(1);
        three.add(3);
        three.add(1);
        three.add(2);
        three.add(5);
        three.add(1);

        ArrayList<Integer> four = new ArrayList<Integer>();
        four.add(11);
        four.add(10);
        four.add(20);
        four.add(20);

        ArrayList<Integer> five = new ArrayList<Integer>();
        five.add(90);
        five.add(3);
        five.add(2);
        five.add(2);
        five.add(1);
        five.add(3);
        five.add(1);
        five.add(2);
        five.add(5);
        five.add(1);

        FCFS sim = new FCFS();
        sim.setProcesses(one);
        sim.setProcesses(two);
        sim.setProcesses(three);
        sim.setProcesses(four);
        sim.setProcesses(five);
        //sim.run();
        //sim.printStats();

        VRR simOne = new VRR(3);
        simOne.setProcesses(one);
        simOne.setProcesses(two);
        simOne.setProcesses(three);
        simOne.setProcesses(four);
        simOne.setProcesses(five);
        //simOne.run();
        //simOne.printStats();

        SRT simTwo = new SRT(0.8, false);
        simTwo.setProcesses(one);
        simTwo.setProcesses(two);
        simTwo.setProcesses(three);
        simTwo.setProcesses(four);
        simTwo.setProcesses(five);
        //simTwo.run();
        //simTwo.printStats();

        HRRN Three = new HRRN(0.8, true);
        Three.setProcesses(one);
        Three.setProcesses(two);
        Three.setProcesses(three);
        Three.setProcesses(four);
        Three.setProcesses(five);
        //Three.run();
        //Three.printStats();

        Feedback simFour = new Feedback(3, 3);
        simFour.setProcesses(one);
        simFour.setProcesses(two);
        simFour.setProcesses(three);
        simFour.setProcesses(four);
        simFour.setProcesses(five);
        //simFour.run();
        //simFour.printStats();

    }

    //------------------------------------------------------------------------------------------------------------------------------------------------
    public static class Process {
        boolean ready = false;
        ArrayList<Integer> activities;
        ArrayList<Integer> responseTimes = new ArrayList<Integer>();
        int pid;
        int activityPointer = 1;
        int quantum = 3;
        double sN;
        int arrivalTime;
        double serviceTime;
        double tN;
        int statServiceTime;
        int startTime = -1;
        int finishTime;
        int turnaroundTime;
        double nTurnaroundTime;
        double aResponseTime;
        int resposeTime;
        double responseRatio;
        int waitTime;
        int priority;
        boolean complete = false;

        public Process(ArrayList<Integer> givenActivities) {
            activities = givenActivities;
            arrivalTime = activities.get(0);
            serviceTime = 0;
            sN = activities.get(1);
        }

        public void addActivity(int duration) {
            activities.add(duration);
        }

        public void updateServiceTime() {
            for (int i = 1; i < activities.size(); i++) {
                if (i % 2 == 1) {
                    serviceTime += activities.get(i);
                }
            }
            statServiceTime = (int) serviceTime;
        }

        public void expAverage(double alpha, boolean given) {
            if (given == false) {
                serviceTime = (alpha * tN) + ((1 - alpha) * (sN));
                sN = serviceTime;
            }
        }

        public void updateActivity() {
            activities.set(activityPointer, activities.get(activityPointer) - 1);
        }

        public void setStartTime(int startTime) {
            if (this.startTime < 0) {
                this.startTime = startTime;
            }
        }

        public void setFinishTime(int finishTime) {
            this.finishTime = finishTime;
        }

        public void setTurnaroundTime() {
            turnaroundTime = finishTime - arrivalTime;
        }

        public void setNormalizedTurnaroundTime() {
            nTurnaroundTime = (double) turnaroundTime / (double) statServiceTime;
        }

        public void calculateResponseRatio(int wait) {
            responseRatio = ((double) (statServiceTime + wait) / statServiceTime);
        }

        public void calculateAverageResponseTime() {
            double sum = 0;
            int denom = 0;
            for (int i = 0; i < responseTimes.size(); i++) {
                if (responseTimes.get(i) != 0) {
                    sum += responseTimes.get(i);
                    denom++;
                }
            }
            if (sum == 0 && denom == 0) {
                aResponseTime = 0;
            } else {
                aResponseTime = (sum / denom);
            }
        }

        public void setPid(int pid) {
            this.pid = pid;
        }
    }

    //------------------------------------------------------------------------------------------------------------------------------------------------
    public static class FCFS {
        int clock = 0;
        Process currentProcess;
        boolean cpuOccupied = false;
        int processCount = 0;
        ArrayList<Process> processes = new ArrayList<Process>();
        ArrayList<Process> processeQueue = new ArrayList<Process>();
        ArrayList<Process> blockedProcesses = new ArrayList<Process>();


        public void setProcesses(ArrayList<Integer> activities) {
            Process newProcess = new Process(activities);
            newProcess.setPid(processCount);
            newProcess.updateServiceTime();
            processCount++;
            processes.add(newProcess);
        }

        public void printStats() {
            double systemTurnarouud = 0;
            double systemNormalized = 0;
            double systemResponse = 0;

            for (int i = 0; i < processes.size(); i++) {
                System.out.println("\nFor process " + i + ":");
                System.out.println("Arrival Time: " + processes.get(i).arrivalTime);
                System.out.println("Service Time: " + processes.get(i).serviceTime);
                System.out.println("Start Time: " + processes.get(i).startTime);
                System.out.println("Finish Time: " + processes.get(i).finishTime);

                processes.get(i).setTurnaroundTime();
                systemTurnarouud += processes.get(i).turnaroundTime;
                System.out.println("Turnaround Time: " + processes.get(i).turnaroundTime);

                processes.get(i).setNormalizedTurnaroundTime();
                systemNormalized += processes.get(i).nTurnaroundTime;
                System.out.println("Normalized Turnaround Time: " + processes.get(i).nTurnaroundTime);

                processes.get(i).calculateAverageResponseTime();
                systemResponse += processes.get(i).aResponseTime;
                System.out.println("Average Response Time: " + processes.get(i).aResponseTime + "\n");
            }

            System.out.println("System Wide Statistics: ");
            System.out.println("Mean Turnaround Time: " + (systemTurnarouud / processes.size()));
            System.out.println("Mean Normalized Turnaround Time: " + (systemNormalized / processes.size()));
            System.out.println("Mean Average Response Time: " + (systemResponse / processes.size()));
        }

        public void run() {
            while (clock < 235) {
                for (int i = 0; i < processes.size(); i++) {
                    if (processes.get(i).arrivalTime == clock) {
                        System.out.println("At time " + clock + ", ARRIVE Event for process " + processes.get(i).pid);
                        processes.get(i).ready = true;
                        processeQueue.add(processes.get(i));
                        processes.get(i).resposeTime = 0;
                    }
                }
                for (int i = 0; i < processeQueue.size(); i++) {
                    if (cpuOccupied == false && processeQueue.get(i).ready == true) {
                        currentProcess = processeQueue.get(i);
                        processeQueue.remove(i);
                        cpuOccupied = true;
                        System.out.println("Dispatch " + currentProcess.pid);
                        currentProcess.setStartTime(clock);
                        currentProcess.responseTimes.add(currentProcess.resposeTime);
                        currentProcess.resposeTime = 0;
                        break;
                    }
                }

                if (cpuOccupied == true && currentProcess.activities.get(currentProcess.activityPointer) == 0) {
                    if (currentProcess.activityPointer == currentProcess.activities.size() - 1) {
                        System.out.println("At time " + clock + ", EXIT Event for process " + currentProcess.pid);
                        currentProcess.setFinishTime(clock);
                        cpuOccupied = false;
                    } else {
                        currentProcess.activityPointer++;
                        System.out.println("At time " + clock + ", BLOCK Event for process " + currentProcess.pid);
                        currentProcess.ready = false;
                        blockedProcesses.add(currentProcess);
                        cpuOccupied = false;
                        currentProcess.resposeTime = 0;
                    }

                    for (int i = 0; i < processeQueue.size(); i++) {
                        if (cpuOccupied == false && processeQueue.get(i).ready == true) {
                            currentProcess = processeQueue.get(i);
                            processeQueue.remove(i);
                            cpuOccupied = true;
                            System.out.println("Dispatch " + currentProcess.pid);
                            currentProcess.setStartTime(clock);
                            currentProcess.responseTimes.add(currentProcess.resposeTime);
                            currentProcess.resposeTime = 0;
                            break;
                        }
                    }
                }

                for (int i = 0; i < blockedProcesses.size(); i++) {
                    if (blockedProcesses.get(i).activities.get(blockedProcesses.get(i).activityPointer) == 0) {
                        System.out.println("At time " + clock + ", UNBLOCK Event for process " + blockedProcesses.get(i).pid);
                        blockedProcesses.get(i).ready = true;
                        blockedProcesses.get(i).activityPointer++;
                        if (cpuOccupied == false) {
                            currentProcess = blockedProcesses.get(i);
                            System.out.println("Dispatch " + currentProcess.pid);
                            currentProcess.setStartTime(clock);
                            currentProcess.responseTimes.add(currentProcess.resposeTime);
                            currentProcess.resposeTime = 0;
                            cpuOccupied = true;
                        } else {
                            processeQueue.add(blockedProcesses.get(i));
                        }
                        blockedProcesses.remove(i);
                        i--;
                    } else {
                        blockedProcesses.get(i).updateActivity();
                    }
                }

                for (int i = 0; i < processeQueue.size(); i++) {
                    processeQueue.get(i).resposeTime++;
                }

                if (cpuOccupied == true) {
                    currentProcess.updateActivity();
                }
                clock++;
            }
        }
    }

    //------------------------------------------------------------------------------------------------------------------------------------------------
    public static class VRR {
        int clock = 0;
        int givenQuantum = 3;
        Process currentProcess;
        boolean cpuOccupied = false;
        int processCount = 0;
        ArrayList<Process> processes = new ArrayList<Process>();
        ArrayList<Process> readyQueue = new ArrayList<Process>();
        ArrayList<Process> blockedProcesses = new ArrayList<Process>();
        ArrayList<Process> auxQueue = new ArrayList<Process>();

        public VRR(int q) {
            givenQuantum = q;
        }

        public void setProcesses(ArrayList<Integer> activities) {
            Process newProcess = new Process(activities);
            newProcess.setPid(processCount);
            newProcess.updateServiceTime();
            processCount++;
            processes.add(newProcess);
        }

        public void printStats() {
            double systemTurnarouud = 0;
            double systemNormalized = 0;
            double systemResponse = 0;

            for (int i = 0; i < processes.size(); i++) {
                System.out.println("\nFor process " + i + ":");
                System.out.println("Arrival Time: " + processes.get(i).arrivalTime);
                System.out.println("Service Time: " + processes.get(i).serviceTime);
                System.out.println("Start Time: " + processes.get(i).startTime);
                System.out.println("Finish Time: " + processes.get(i).finishTime);

                processes.get(i).setTurnaroundTime();
                systemTurnarouud += processes.get(i).turnaroundTime;
                System.out.println("Turnaround Time: " + processes.get(i).turnaroundTime);

                processes.get(i).setNormalizedTurnaroundTime();
                systemNormalized += processes.get(i).nTurnaroundTime;
                System.out.println("Normalized Turnaround Time: " + processes.get(i).nTurnaroundTime);

                processes.get(i).calculateAverageResponseTime();
                systemResponse += processes.get(i).aResponseTime;
                System.out.println("Average Response Time: " + processes.get(i).aResponseTime + "\n");
            }

            System.out.println("System Wide Statistics: ");
            System.out.println("Mean Turnaround Time: " + (systemTurnarouud / processes.size()));
            System.out.println("Mean Normalized Turnaround Time: " + (systemNormalized / processes.size()));
            System.out.println("Mean Average Response Time: " + (systemResponse / processes.size()));
        }

        public void run() {
            while (clock < 235) {
                for (int i = 0; i < processes.size(); i++) {
                    if (processes.get(i).arrivalTime == clock) {
                        System.out.println("At time " + clock + ", ARRIVE Event for process " + processes.get(i).pid);
                        processes.get(i).ready = true;
                        processes.get(i).quantum = givenQuantum;
                        readyQueue.add(processes.get(i));
                        processes.get(i).resposeTime = 0;
                        if (cpuOccupied == false) {
                            currentProcess = readyQueue.get(i);
                            readyQueue.remove(i);
                            cpuOccupied = true;
                            System.out.println("Dispatch " + currentProcess.pid);
                        }
                    }
                }

                if (currentProcess.quantum == 0 && cpuOccupied == true && currentProcess.activities.get(currentProcess.activityPointer) != 0) {
                    System.out.println("At time " + clock + ", TIMEOUT Event for process " + currentProcess.pid);
                    cpuOccupied = false;
                    currentProcess.quantum = givenQuantum;
                    readyQueue.add(currentProcess);
                } else if (cpuOccupied == true && currentProcess.activities.get(currentProcess.activityPointer) == 0) {
                    if (currentProcess.activityPointer == currentProcess.activities.size() - 1) {
                        System.out.println("At time " + clock + ", EXIT Event for process " + currentProcess.pid);
                        currentProcess.setFinishTime(clock);
                        cpuOccupied = false;
                    } else {
                        currentProcess.activityPointer++;
                        System.out.println("At time " + clock + ", BLOCK Event for process " + currentProcess.pid);
                        currentProcess.ready = false;
                        blockedProcesses.add(currentProcess);
                        cpuOccupied = false;
                    }
                }

                for (int i = 0; i < blockedProcesses.size(); i++) {
                    if (blockedProcesses.get(i).activities.get(blockedProcesses.get(i).activityPointer) == 0) {
                        System.out.println("At time " + clock + ", UNBLOCK Event for process " + blockedProcesses.get(i).pid);
                        blockedProcesses.get(i).ready = true;
                        blockedProcesses.get(i).activityPointer++;
                        if (cpuOccupied == false) {
                            currentProcess = blockedProcesses.get(i);
                            System.out.println("Dispatch " + currentProcess.pid);
                            currentProcess.setStartTime(clock);
                            currentProcess.responseTimes.add(currentProcess.resposeTime);
                            currentProcess.resposeTime = 0;
                            cpuOccupied = true;
                            if (currentProcess.quantum == 0) {
                                currentProcess.quantum = givenQuantum;
                            }
                        } else {
                            if (blockedProcesses.get(i).quantum == 0) {
                                blockedProcesses.get(i).quantum = givenQuantum;
                                readyQueue.add(blockedProcesses.get(i));
                            } else {
                                auxQueue.add(blockedProcesses.get(i));
                            }
                        }
                        blockedProcesses.remove(i);
                        i--;
                    } else {
                        blockedProcesses.get(i).updateActivity();
                    }
                }

                for (int i = 0; i < auxQueue.size(); i++) {
                    if (cpuOccupied == false && auxQueue.get(i).ready == true) {
                        currentProcess = auxQueue.get(i);
                        auxQueue.remove(i);
                        cpuOccupied = true;
                        System.out.println("Dispatch " + currentProcess.pid);
                        currentProcess.setStartTime(clock);
                        currentProcess.responseTimes.add(currentProcess.resposeTime);
                        currentProcess.resposeTime = 0;
                        break;
                    }
                }

                for (int i = 0; i < readyQueue.size(); i++) {
                    if (cpuOccupied == false && readyQueue.get(i).ready == true) {
                        currentProcess = readyQueue.get(i);
                        readyQueue.remove(i);
                        cpuOccupied = true;
                        System.out.println("Dispatch " + currentProcess.pid);
                        currentProcess.setStartTime(clock);
                        currentProcess.responseTimes.add(currentProcess.resposeTime);
                        currentProcess.resposeTime = 0;
                        break;
                    }
                }


                for (int i = 0; i < readyQueue.size(); i++) {
                    readyQueue.get(i).resposeTime++;
                }
                for (int i = 0; i < auxQueue.size(); i++) {
                    auxQueue.get(i).resposeTime++;
                }

                if (cpuOccupied == true) {
                    currentProcess.updateActivity();
                    currentProcess.quantum--;
                }
                clock++;
            }
        }
    }

    //------------------------------------------------------------------------------------------------------------------------------------------------
    public static class SRT {
        int clock = 0;
        boolean decision = false;
        double alpha = 0.8;
        Process currentProcess;
        boolean cpuOccupied = false;
        int processCount = 0;
        ArrayList<Process> processes = new ArrayList<Process>();
        ArrayList<Process> readyQueue = new ArrayList<Process>();
        ArrayList<Process> blockedProcesses = new ArrayList<Process>();

        public SRT(double a, boolean given) {
            alpha = a;
            decision = given;
        }

        public void printStats() {
            double systemTurnarouud = 0;
            double systemNormalized = 0;
            double systemResponse = 0;

            for (int i = 0; i < processes.size(); i++) {
                System.out.println("\nFor process " + i + ":");
                System.out.println("Arrival Time: " + processes.get(i).arrivalTime);
                System.out.println("Service Time: " + processes.get(i).statServiceTime);
                System.out.println("Start Time: " + processes.get(i).startTime);
                System.out.println("Finish Time: " + processes.get(i).finishTime);

                processes.get(i).setTurnaroundTime();
                systemTurnarouud += processes.get(i).turnaroundTime;
                System.out.println("Turnaround Time: " + processes.get(i).turnaroundTime);

                processes.get(i).setNormalizedTurnaroundTime();
                systemNormalized += processes.get(i).nTurnaroundTime;
                System.out.println("Normalized Turnaround Time: " + processes.get(i).nTurnaroundTime);

                processes.get(i).calculateAverageResponseTime();
                systemResponse += processes.get(i).aResponseTime;
                System.out.println("Average Response Time: " + processes.get(i).aResponseTime + "\n");
            }

            System.out.println("System Wide Statistics: ");
            System.out.println("Mean Turnaround Time: " + (systemTurnarouud / processes.size()));
            System.out.println("Mean Normalized Turnaround Time: " + (systemNormalized / processes.size()));
            System.out.println("Mean Average Response Time: " + (systemResponse / processes.size()));
        }

        public void setProcesses(ArrayList<Integer> activities) {
            Process newProcess = new Process(activities);
            newProcess.setPid(processCount);
            newProcess.updateServiceTime();
            if (decision == false) {
                newProcess.serviceTime = newProcess.sN;
            }
            processCount++;
            processes.add(newProcess);
        }

        public void run() {
            while (clock < 235) {
                for (int i = 0; i < processes.size(); i++) {
                    if (processes.get(i).arrivalTime == clock) {
                        System.out.println("At time " + clock + ", ARRIVE Event for process " + processes.get(i).pid);
                        processes.get(i).ready = true;
                        readyQueue.add(processes.get(i));
                        processes.get(i).resposeTime = 0;
                        if (cpuOccupied == false) {
                            currentProcess = readyQueue.get(i);
                            readyQueue.remove(i);
                            cpuOccupied = true;
                            System.out.println("Dispatch " + currentProcess.pid);
                            currentProcess.setStartTime(clock);
                        }
                    }
                }

                if (cpuOccupied == true && currentProcess.activities.get(currentProcess.activityPointer) == 0) {
                    if (currentProcess.activityPointer == currentProcess.activities.size() - 1) {
                        exit();
                    } else {
                        block();
                    }
                }

                unblock();
                compare();

                if (cpuOccupied == true) {
                    currentProcess.updateActivity();
                    currentProcess.serviceTime--;
                    currentProcess.tN++;
                }

                for (int i = 0; i < readyQueue.size(); i++) {
                    readyQueue.get(i).resposeTime++;
                }

                clock++;
            }
        }

        public void block() {
            currentProcess.expAverage(alpha, decision);
            currentProcess.tN = 0;
            currentProcess.activityPointer++;
            System.out.println("At time " + clock + ", BLOCK Event for process " + currentProcess.pid);
            currentProcess.ready = false;
            blockedProcesses.add(currentProcess);
            cpuOccupied = false;
        }

        public void unblock() {
            for (int i = 0; i < blockedProcesses.size(); i++) {
                if (blockedProcesses.get(i).activities.get(blockedProcesses.get(i).activityPointer) == 0) {
                    System.out.println("At time " + clock + ", UNBLOCK Event for process " + blockedProcesses.get(i).pid);
                    blockedProcesses.get(i).ready = true;
                    blockedProcesses.get(i).activityPointer++;
                    if (cpuOccupied == false) {
                        readyQueue.add(blockedProcesses.get(i));
                        compare();
                    } else {
                        readyQueue.add(blockedProcesses.get(i));
                    }
                    blockedProcesses.remove(i);
                    i--;
                } else {
                    blockedProcesses.get(i).updateActivity();
                }
            }
        }

        public void exit() {
            System.out.println("At time " + clock + ", EXIT Event for process " + currentProcess.pid);
            currentProcess.setFinishTime(clock);
            currentProcess.complete = true;
            cpuOccupied = false;
            compare();
        }

        public void compare() {
            Process preempt = null;
            if (cpuOccupied == true) {
                preempt = currentProcess;
            } else if (!readyQueue.isEmpty()) {
                preempt = readyQueue.get(0);
            }


            int index = 0;
            for (int i = 0; i < readyQueue.size(); i++) {
                if (preempt.serviceTime > readyQueue.get(i).serviceTime) {
                    preempt = readyQueue.get(i);
                    index = i;
                }
            }

            if (!(preempt == null) && !(preempt.equals(currentProcess))) {
                if (!blockedProcesses.contains(currentProcess) && currentProcess.complete != true) {
                    System.out.println("At time " + clock + ", TIMEOUT Event for process " + currentProcess.pid);
                    readyQueue.add(currentProcess);
                }
                currentProcess = preempt;
                readyQueue.remove(index);
                cpuOccupied = true;
                System.out.println("Dispatch " + currentProcess.pid);
                currentProcess.setStartTime(clock);
                currentProcess.responseTimes.add(currentProcess.resposeTime);
                currentProcess.resposeTime = 0;
            } else if (!(preempt == null) && preempt.equals(currentProcess) && cpuOccupied == false) {
                readyQueue.remove(index);
                cpuOccupied = true;
                System.out.println("Dispatch " + currentProcess.pid);
                currentProcess.setStartTime(clock);
                currentProcess.responseTimes.add(currentProcess.resposeTime);
                currentProcess.resposeTime = 0;
            }
        }
    }

    //------------------------------------------------------------------------------------------------------------------------------------------------
    public static class HRRN {
        int clock = 0;
        boolean decision = true;
        double alpha = 0.8;
        Process currentProcess;
        boolean cpuOccupied = false;
        int processCount = 0;
        ArrayList<Process> processes = new ArrayList<Process>();
        ArrayList<Process> readyQueue = new ArrayList<Process>();
        ArrayList<Process> blockedProcesses = new ArrayList<Process>();

        public HRRN(double a, boolean given) {
            alpha = a;
            decision = given;
        }

        public void printStats() {
            double systemTurnarouud = 0;
            double systemNormalized = 0;
            double systemResponse = 0;

            for (int i = 0; i < processes.size(); i++) {
                System.out.println("\nFor process " + i + ":");
                System.out.println("Arrival Time: " + processes.get(i).arrivalTime);
                System.out.println("Service Time: " + processes.get(i).statServiceTime);
                System.out.println("Start Time: " + processes.get(i).startTime);
                System.out.println("Finish Time: " + processes.get(i).finishTime);

                processes.get(i).setTurnaroundTime();
                systemTurnarouud += processes.get(i).turnaroundTime;
                System.out.println("Turnaround Time: " + processes.get(i).turnaroundTime);

                processes.get(i).setNormalizedTurnaroundTime();
                systemNormalized += processes.get(i).nTurnaroundTime;
                System.out.println("Normalized Turnaround Time: " + processes.get(i).nTurnaroundTime);

                processes.get(i).calculateAverageResponseTime();
                systemResponse += processes.get(i).aResponseTime;
                System.out.println("Average Response Time: " + processes.get(i).aResponseTime + "\n");
            }

            System.out.println("System Wide Statistics: ");
            System.out.println("Mean Turnaround Time: " + (systemTurnarouud / processes.size()));
            System.out.println("Mean Normalized Turnaround Time: " + (systemNormalized / processes.size()));
            System.out.println("Mean Average Response Time: " + (systemResponse / processes.size()));
        }

        public void setProcesses(ArrayList<Integer> activities) {
            Process newProcess = new Process(activities);
            newProcess.setPid(processCount);
            newProcess.updateServiceTime();
            if (decision == false) {
                newProcess.serviceTime = newProcess.sN;
            }
            processCount++;
            processes.add(newProcess);
        }

        public void run() {
            while (clock < 235) {
                for (int i = 0; i < processes.size(); i++) {
                    if (processes.get(i).arrivalTime == clock) {
                        System.out.println("At time " + clock + ", ARRIVE Event for process " + processes.get(i).pid);
                        processes.get(i).ready = true;
                        readyQueue.add(processes.get(i));
                        processes.get(i).resposeTime = 0;
                        processes.get(i).waitTime = 0;
                        if (cpuOccupied == false) {
                            currentProcess = readyQueue.get(i);
                            readyQueue.remove(i);
                            cpuOccupied = true;
                            System.out.println("Dispatch " + currentProcess.pid);
                            currentProcess.setStartTime(clock);
                        }
                    }
                }

                if (cpuOccupied == true && currentProcess.activities.get(currentProcess.activityPointer) == 0) {
                    if (currentProcess.activityPointer == currentProcess.activities.size() - 1) {
                        exit();
                    } else {
                        block();
                    }
                }

                unblock();
                dispatch();

                if (cpuOccupied == true) {
                    currentProcess.updateActivity();
                    currentProcess.serviceTime--;
                    currentProcess.tN++;
                }

                for (int i = 0; i < readyQueue.size(); i++) {
                    readyQueue.get(i).resposeTime++;
                    readyQueue.get(i).waitTime++;

                }

                clock++;
            }
        }

        public void block() {
            currentProcess.expAverage(alpha, decision);
            currentProcess.tN = 0;
            currentProcess.activityPointer++;
            System.out.println("At time " + clock + ", BLOCK Event for process " + currentProcess.pid);
            currentProcess.ready = false;
            blockedProcesses.add(currentProcess);
            cpuOccupied = false;
        }

        public void unblock() {
            for (int i = 0; i < blockedProcesses.size(); i++) {
                if (blockedProcesses.get(i).activities.get(blockedProcesses.get(i).activityPointer) == 0) {
                    System.out.println("At time " + clock + ", UNBLOCK Event for process " + blockedProcesses.get(i).pid);
                    blockedProcesses.get(i).ready = true;
                    blockedProcesses.get(i).activityPointer++;
                    if (cpuOccupied == false) {
                        readyQueue.add(blockedProcesses.get(i));
                        dispatch();
                    } else {
                        readyQueue.add(blockedProcesses.get(i));
                    }
                    blockedProcesses.remove(i);
                    i--;
                } else {
                    blockedProcesses.get(i).updateActivity();
                }
            }
        }

        public void exit() {
            System.out.println("At time " + clock + ", EXIT Event for process " + currentProcess.pid);
            currentProcess.setFinishTime(clock);
            currentProcess.complete = true;
            cpuOccupied = false;
            dispatch();
        }

        public void dispatch() {
            if (cpuOccupied == false && !readyQueue.isEmpty()) {
                int index = 0;
                Process next = readyQueue.get(0);
                next.calculateResponseRatio((next.waitTime));
                for (int i = 0; i < readyQueue.size(); i++) {
                    readyQueue.get(i).calculateResponseRatio((readyQueue.get(i).waitTime));
                    if (next.responseRatio < readyQueue.get(i).responseRatio) {
                        next = readyQueue.get(i);
                        index = i;
                    }
                }
                currentProcess = next;
                readyQueue.remove(index);
                cpuOccupied = true;
                System.out.println("Dispatch " + currentProcess.pid);
                currentProcess.setStartTime(clock);
                currentProcess.responseTimes.add(currentProcess.resposeTime);
                currentProcess.resposeTime = 0;
            }
        }
    }

    //------------------------------------------------------------------------------------------------------------------------------------------------
    public static class Feedback {
        int clock = 0;
        int givenQuantum = 3;
        int priority = 0;
        Process currentProcess;
        boolean cpuOccupied = false;
        int processCount = 0;
        ArrayList<Process> processes = new ArrayList<Process>();
        ArrayList<Process> readyQueue = new ArrayList<Process>();
        ArrayList<Process> blockedProcesses = new ArrayList<Process>();

        public Feedback(int q, int prio) {
            givenQuantum = q;
            priority = prio;
        }

        public void setProcesses(ArrayList<Integer> activities) {
            Process newProcess = new Process(activities);
            newProcess.setPid(processCount);
            newProcess.updateServiceTime();
            newProcess.priority = priority;
            processCount++;
            processes.add(newProcess);
        }

        public void printStats() {
            double systemTurnarouud = 0;
            double systemNormalized = 0;
            double systemResponse = 0;

            for (int i = 0; i < processes.size(); i++) {
                System.out.println("\nFor process " + i + ":");
                System.out.println("Arrival Time: " + processes.get(i).arrivalTime);
                System.out.println("Service Time: " + processes.get(i).serviceTime);
                System.out.println("Start Time: " + processes.get(i).startTime);
                System.out.println("Finish Time: " + processes.get(i).finishTime);

                processes.get(i).setTurnaroundTime();
                systemTurnarouud += processes.get(i).turnaroundTime;
                System.out.println("Turnaround Time: " + processes.get(i).turnaroundTime);

                processes.get(i).setNormalizedTurnaroundTime();
                systemNormalized += processes.get(i).nTurnaroundTime;
                System.out.println("Normalized Turnaround Time: " + processes.get(i).nTurnaroundTime);

                processes.get(i).calculateAverageResponseTime();
                systemResponse += processes.get(i).aResponseTime;
                System.out.println("Average Response Time: " + processes.get(i).aResponseTime + "\n");
            }

            System.out.println("System Wide Statistics: ");
            System.out.println("Mean Turnaround Time: " + (systemTurnarouud / processes.size()));
            System.out.println("Mean Normalized Turnaround Time: " + (systemNormalized / processes.size()));
            System.out.println("Mean Average Response Time: " + (systemResponse / processes.size()));
        }

        public void run() {
            while (clock < 235) {
                for (int i = 0; i < processes.size(); i++) {
                    if (processes.get(i).arrivalTime == clock) {
                        System.out.println("At time " + clock + ", ARRIVE Event for process " + processes.get(i).pid);
                        processes.get(i).ready = true;
                        processes.get(i).quantum = givenQuantum;
                        readyQueue.add(processes.get(i));
                        processes.get(i).resposeTime = 0;
                        if (cpuOccupied == false) {
                            currentProcess = readyQueue.get(i);
                            readyQueue.remove(i);
                            cpuOccupied = true;
                            System.out.println("Dispatch " + currentProcess.pid);
                        }
                    }
                }

                for (int i = 0; i < blockedProcesses.size(); i++) {
                    blockedProcesses.get(i).updateActivity();
                    if (blockedProcesses.get(i).activities.get(blockedProcesses.get(i).activityPointer) == 0) {
                        System.out.println("At time " + clock + ", UNBLOCK Event for process " + blockedProcesses.get(i).pid);
                        blockedProcesses.get(i).ready = true;
                        blockedProcesses.get(i).activityPointer++;
                        if (cpuOccupied == false) {
                            readyQueue.add(blockedProcesses.get(i));
                            dispatch();
                            if (currentProcess.quantum == 0) {
                                currentProcess.quantum = givenQuantum;
                            }
                        } else {
                            if (blockedProcesses.get(i).quantum == 0) {
                            blockedProcesses.get(i).quantum = givenQuantum;
                            }
                            readyQueue.add(blockedProcesses.get(i));
                        }
                        blockedProcesses.remove(i);
                        i--;
                    }
                }

                if (currentProcess.quantum == 0 && cpuOccupied == true && currentProcess.activities.get(currentProcess.activityPointer) != 0) {
                    System.out.println("At time " + clock + ", TIMEOUT Event for process " + currentProcess.pid);
                    cpuOccupied = false;
                    currentProcess.quantum = givenQuantum;
                    if (currentProcess.priority > 1) {
                        currentProcess.priority--;
                    }
                    readyQueue.add(currentProcess);
                    dispatch();

                } else if (cpuOccupied == true && currentProcess.activities.get(currentProcess.activityPointer) == 0) {
                    if (currentProcess.activityPointer == currentProcess.activities.size() - 1) {
                        System.out.println("At time " + clock + ", EXIT Event for process " + currentProcess.pid);
                        currentProcess.setFinishTime(clock);
                        cpuOccupied = false;
                        currentProcess.complete = true;
                    } else {
                        currentProcess.activityPointer++;
                        System.out.println("At time " + clock + ", BLOCK Event for process " + currentProcess.pid);
                        currentProcess.ready = false;
                        blockedProcesses.add(currentProcess);
                        cpuOccupied = false;
                    }
                }

                dispatch();

                for (int i = 0; i < readyQueue.size(); i++) {
                    readyQueue.get(i).resposeTime++;
                }

                if (cpuOccupied == true) {
                    currentProcess.updateActivity();
                    currentProcess.quantum--;
                }
                clock++;
            }
        }

        public void dispatch() {
            for (int i = priority; i >= 1; i--) {
                for (int j = 0; j < readyQueue.size(); j++) {
                    if (cpuOccupied == false && readyQueue.get(j).ready == true && readyQueue.get(j).priority == i && readyQueue.get(j).complete == false) {
                        currentProcess = readyQueue.get(j);
                        readyQueue.remove(j);
                        cpuOccupied = true;
                        System.out.println("Dispatch " + currentProcess.pid);
                        currentProcess.setStartTime(clock);
                        currentProcess.responseTimes.add(currentProcess.resposeTime);
                        currentProcess.resposeTime = 0;
                        currentProcess.quantum = givenQuantum;
                        break;
                    }
                }
            }
        }
    }
}
