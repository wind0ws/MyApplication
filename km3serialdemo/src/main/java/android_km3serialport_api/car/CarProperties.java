package android_km3serialport_api.car;


import android_km3serialport_api.attributeevent.AttrChangedEvent;
import android_km3serialport_api.attributeevent.IAttrChangedListener;
import android_km3serialport_api.attributeevent.ListenerManager;

/**
 * 车辆属性
 */
public class CarProperties {

    private ListenerManager listenerManager;
    public CarProperties() {
        init();
        listenerManager=new ListenerManager();
    }
    
    public static final int CAR_HORN=10;
    public static final int CAR_VELOCITY=11;
    public static final int CAR_DIRECTION=12;
    public static final int LEFT_TURN=13;
    public static final int RIGHT_TURN=14;
    public static final int HIGH_BEAM=15;
    public static final int LOW_BEAM=16;
    public static final int WIDTH_LAMP=17;
    public static final int WARNING_LAMP=18;
    public static final int FOG_LAMP=19;
    public static final int FOOT_BRAKE=20;
    public static final int HAND_BRAKE=21;
    public static final int BRAKE_PRESSURE=22;
    public static final int LIFE_BELT=23;
    public static final int SB_ON_SEAT=24;
    public static final int CAR_DOOR=25;
    public static final int IGNITION_SWITCH=26;
    public static final int CAR_HEAD_STATE=27;
    public static final int CAR_TAIL_STATE=28;
    public static final int ENGINE_STATE=29;
    public static final int ENGINE_SPEED=30;
    public static final int CAR_CLUTCH=31;
    public static final int CAR_GEAR=32;
    public static final int CAR_ACCELERATOR=33;
    public static final int VICE_BRAKE=34;
    public static final int SEAT_ADJUST=35;

    private int carHorn;
    private int carVelocity;
    private int carDirection;
    private int leftTurn;
    private int rightTurn;
    private int highBeam;
    private int lowBeam;
    private int widthLamp;
    private int warningLamp;
    private int fogLamp;
    private int footBrake;
    private int handBrake;
    private int brakePressure;
    private int lifeBelt;
    private int sbOnSeat;
    private int carDoor;
    private int ignitionSwitch;
    private int carHeadState;
    private int carTailState;
    private int engineState;
    private int engineSpeed;
    private int carClutch;
    private int carGear;
    private int carAccelerator;
    private int viceBrake;
    private int seatAdjust;


    protected void init() {
        carHorn = -1;
        carVelocity=-1;
        carDirection=-2;
        leftTurn=-1;
        rightTurn=-1;
        highBeam=-1;
        lowBeam=-1;
        widthLamp=-1;
        warningLamp=-1;
        fogLamp=-1;
        footBrake=-1;
        handBrake=-1;
        brakePressure=-1;
        lifeBelt=-1;
        sbOnSeat=-1;
        carDoor=-1;
        ignitionSwitch=-1;
        carHeadState=-1;
        carTailState=-1;
        engineState=-1;
        engineSpeed=-1;
        carClutch=-1;
        carGear=-2;
        carAccelerator=-1;
        viceBrake=-1;
        seatAdjust=-1;
    }


    public int getSeatAdjust() {
        return seatAdjust;
    }

    public void setSeatAdjust(int seatAdjust) {
        if(fireAEvent(SEAT_ADJUST,this.seatAdjust,seatAdjust)){
        this.seatAdjust = seatAdjust;}
    }

    public int getCarHorn() {
        return carHorn;
    }

    protected void setCarHorn(int carHorn) {
        if(fireAEvent(CAR_HORN,this.carHorn,carHorn)){
        this.carHorn = carHorn;}
    }

    public int getCarVelocity() {
        return carVelocity;
    }

    protected void setCarVelocity(int carVelocity) {
        if(fireAEvent(CAR_VELOCITY,this.carVelocity,carVelocity)){
        this.carVelocity = carVelocity;}
    }

    public int getCarDirection() {
        return carDirection;
    }

    protected void setCarDirection(int carDirection) {
        if(fireAEvent(CAR_DIRECTION,this.carDirection,carDirection)){
        this.carDirection = carDirection;}
    }

    public int getLeftTurn() {
        return leftTurn;
    }

    protected void setLeftTurn(int leftTurn) {
        if(fireAEvent(LEFT_TURN,this.leftTurn,leftTurn)){
        this.leftTurn = leftTurn;}
    }

    public int getRightTurn() {
        return rightTurn;
    }

    protected void setRightTurn(int rightTurn) {
        if(fireAEvent(RIGHT_TURN,this.rightTurn,rightTurn)){
        this.rightTurn = rightTurn;}
    }

    public int getHighBeam() {
        return highBeam;
    }

    protected void setHighBeam(int highBeam) {
        if(fireAEvent(HIGH_BEAM,this.highBeam,highBeam)){
        this.highBeam = highBeam;}
    }

    public int getLowBeam() {
        return lowBeam;
    }

    protected void setLowBeam(int lowBeam) {
        if(fireAEvent(LOW_BEAM,this.lowBeam,lowBeam)){
        this.lowBeam = lowBeam;}
    }

    public int getWidthLamp() {
        return widthLamp;
    }

    protected void setWidthLamp(int widthLamp) {
        if(fireAEvent(WIDTH_LAMP,this.widthLamp,widthLamp)){
        this.widthLamp = widthLamp;}
    }

    public int getWarningLamp() {
        return warningLamp;
    }

    protected void setWarningLamp(int warningLamp) {
        if(fireAEvent(WARNING_LAMP,this.warningLamp,warningLamp)){
        this.warningLamp = warningLamp;}
    }

    public int getFogLamp() {
        return fogLamp;
    }

    protected void setFogLamp(int fogLamp) {
        if(fireAEvent(FOG_LAMP,this.fogLamp,fogLamp)){
        this.fogLamp = fogLamp;}
    }

    public int getFootBrake() {
        return footBrake;
    }

    protected void setFootBrake(int footBrake) {
        if(fireAEvent(FOOT_BRAKE,this.footBrake,footBrake)){
        this.footBrake = footBrake;}
    }

    public int getHandBrake() {
        return handBrake;
    }

    protected void setHandBrake(int handBrake) {
        if(fireAEvent(HAND_BRAKE,this.handBrake,handBrake)){
        this.handBrake = handBrake;}
    }

    public int getBrakePressure() {
        return brakePressure;
    }

    protected void setBrakePressure(int brakePressure) {
        if(fireAEvent(BRAKE_PRESSURE,this.brakePressure,brakePressure)){
        this.brakePressure = brakePressure;}
    }

    public int getLifeBelt() {
        return lifeBelt;
    }

    protected void setLifeBelt(int lifeBelt) {
        if(fireAEvent(LIFE_BELT,this.lifeBelt,lifeBelt)){
        this.lifeBelt = lifeBelt;}
    }

    public int getSbOnSeat() {
        return sbOnSeat;
    }

    protected void setSbOnSeat(int sbOnSeat) {
        if(fireAEvent(SB_ON_SEAT,this.sbOnSeat,sbOnSeat)){
        this.sbOnSeat = sbOnSeat;}
    }

    public int getCarDoor() {
        return carDoor;
    }

    protected void setCarDoor(int carDoor) {
        if(fireAEvent(CAR_DOOR,this.carDoor,carDoor)){
        this.carDoor = carDoor;}
    }

    public int getIgnitionSwitch() {
        return ignitionSwitch;
    }

    protected void setIgnitionSwitch(int ignitionSwitch) {
        if(fireAEvent(IGNITION_SWITCH,this.ignitionSwitch,ignitionSwitch)){
        this.ignitionSwitch = ignitionSwitch;}
    }

    public int getCarHeadState() {
        return carHeadState;
    }

    protected void setCarHeadState(int carHeadState) {
        if(fireAEvent(CAR_HEAD_STATE,this.carHeadState,carHeadState)){
        this.carHeadState = carHeadState;}
    }

    public int getCarTailState() {
        return carTailState;
    }

    protected void setCarTailState(int carTailState) {
        if(fireAEvent(CAR_TAIL_STATE,this.carTailState,carTailState)){
        this.carTailState = carTailState;}
    }

    public int getEngineState() {
        return engineState;
    }

    protected void setEngineState(int engineState) {
        if(fireAEvent(ENGINE_STATE,this.engineState,engineState)){
        this.engineState = engineState;}
    }

    public int getEngineSpeed() {
        return engineSpeed;
    }

    protected void setEngineSpeed(int engineSpeed) {
        if(fireAEvent(ENGINE_SPEED,this.engineSpeed,engineSpeed)){
        this.engineSpeed = engineSpeed;}
    }

    public int getCarClutch() {
        return carClutch;
    }

    protected void setCarClutch(int carClutch) {
        if(fireAEvent(CAR_CLUTCH,this.carClutch,carClutch)){
        this.carClutch = carClutch;}
    }

    public int getCarGear() {
        return carGear;
    }

    protected void setCarGear(int carGear) {
        if(fireAEvent(CAR_GEAR,this.carGear,carGear)){
        this.carGear = carGear;}
    }

    public int getCarAccelerator() {
        return carAccelerator;
    }

    protected void setCarAccelerator(int carAccelerator) {
        if(fireAEvent(CAR_ACCELERATOR,this.carAccelerator,carAccelerator)){
        this.carAccelerator = carAccelerator;}
    }

    public int getViceBrake() {
        return viceBrake;
    }

    protected void setViceBrake(int viceBrake) {
        if(fireAEvent(VICE_BRAKE,this.viceBrake,viceBrake)){
        this.viceBrake = viceBrake;
        }
    }


    public void addListener(IAttrChangedListener listener){
        listenerManager.addListener(listener);
    }

    public void removeListener(IAttrChangedListener listener){
        listenerManager.removeListener(listener);
    }

    public boolean fireAEvent(int sender,int oldVal,int newVal){
        if(oldVal!=newVal){
         listenerManager.fireAEvent(new AttrChangedEvent(sender,oldVal,newVal));
         return true;
        }
        return false;
    }


}

