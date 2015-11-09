package android_km3serialport_api.car;

import android.util.Log;

import java.util.ArrayList;

import static android_km3serialport_api.SerialPortBase.convertIntegers;

/**
 * 车载主板数据分析器.
 */
public class CarDataParser {
    private static final String TAG="车载板解析";
    public static final int FRAME_HEAD_PC2SERIAL=0xff;
    public static final int FRAME_TAIL_PC2SERIAL=0xfe;
    public static final int FRAME_HEAD_SERIAL2PC=0xfd;
    public static final int FRAME_TAIL_SERIAL2PC=0xfc;
    private CarProperties carProperties;
    private ArrayList<Integer> oneFrameList;
    private ArrayList<Integer> oneFrameListWithoutSpecialChar;

    public CarDataParser(CarProperties carProperties) {
        this.carProperties=carProperties;
        oneFrameList =new ArrayList<>();
        oneFrameListWithoutSpecialChar=new ArrayList<>();
    }

    public CarProperties getCarProperties() {
        return this.carProperties;
    }

    /**
     * 从队列中获取帧（可能包含转义字符0xfb）
      * @param intFIFO 队列
     * @param frameHead 帧头，比如0xfd
     * @param frameTail 帧尾，比如0xfc
     * @return 返回完整帧
     */
    public int[] getOneFrameFromQueue(IntFIFO intFIFO,int frameHead,int frameTail){
        int frameHeadIndex=intFIFO.getFirstItemIndex(frameHead);
        int frameTailIndex=intFIFO.getFirstItemIndex(frameTail);
      /*  if(frameHeadIndex==-1||frameTailIndex-frameHeadIndex<10) {
            if(frameTailIndex==-1){//没找到帧尾，即使找到帧头也凑不成一个完整帧，队列全部清掉
                intFIFO.removeAll();
                return null;
            }
            try {
                intFIFO.remove(0, frameHeadIndex);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }*/
        if(frameHeadIndex==-1) {
            if(frameTailIndex==-1){
           //没找到帧尾，即使找到帧头也凑不成一个完整帧，队列全部清掉
            intFIFO.removeAll();
            return null;
            }else {//没找到帧头，但是有帧尾，移除到帧尾的位置
                try {
                    intFIFO.remove(0, frameTailIndex + 1);
                } catch (InterruptedException e) {
                    Log.d(TAG, "intFIFO中移除数据出错。（从0移除到帧尾）");
                    e.printStackTrace();
                }
            }
        }else {
            if(frameTailIndex==-1){
                intFIFO.removeAll();
                return null;
            }
        }

        oneFrameList.clear();
        boolean flag=false;
        int frameCode=0;
        while (!intFIFO.isEmpty()){
            try {
                frameCode=intFIFO.remove();
            } catch (InterruptedException e) {
                Log.w(TAG,"从intFIFO中取出数据出现问题");
                e.printStackTrace();
            }
            if(frameCode==frameHead){
                flag=true;
            }
            if(flag){
                oneFrameList.add(frameCode);
            }
            if(frameCode==frameTail){
                //flag=false;
                break;
            }
        }
       return convertIntegers(oneFrameList);
    }

    /**
     * 从一个完整帧中去除转义字符（0xfb），转换成标准帧
     * @param oneFrame 一个完整帧（包含帧头0xfd，帧尾0xfc,可能包含转义字符）
     * @return 返回一个完整帧（去除转义字符）
     */
    public int[] getOneFrameWithoutSpecialChar(int[] oneFrame){
        if(oneFrame==null||oneFrame.length<13){
            Log.d(TAG,"获取的帧为空或者帧大小不够13个");
            return null;//不够完整一个帧
        }
        oneFrameListWithoutSpecialChar.clear();
 //       int[] data=new int[13];
//        if(oneFrame.length==data.length){
//            //data=oneFrame.clone();//注意这里，可能复制出错
//        }else{
            //int dataPos=0;
            int dataCode;
            boolean needIgnore=false;
            for(int i=0;i<oneFrame.length;i++){
                if(needIgnore){
                    needIgnore=false;
                    continue;
                }
               dataCode=oneFrame[i];
               if(dataCode!=0xfb){
                   oneFrameListWithoutSpecialChar.add(dataCode);
                    //data[dataPos]=dataCode;
                }else {
                   if(i+1<oneFrame.length) {
                       oneFrameListWithoutSpecialChar.add(handleSpecialHex(oneFrame[i + 1]));
                       // data[dataPos] = handleSpecialHex(oneFrame[i+1]);
                       needIgnore = true;//因为发现了转义字符，所以在接下来要忽略下一个字符
                   }
                }
                //dataPos++;
            }
       // }
       // return data;
        return convertIntegers(oneFrameListWithoutSpecialChar);
    }

    /**
     * 获取帧中的功能码
     * @param frameFromQueue 从队列中获取的帧（尚未去除转义字符或已去除）
     * @param frameHead 帧头，比如0xfd
     * @param  destinationCode 目的地址 0x88
     * @param unitCode 车载板的第几个模块 0x01
     * @return 返回功能码,返回-1表明这个帧与你的帧头或目的地址或模块地址不匹配
     */
    public int getFunctionCode(int[] frameFromQueue,int frameHead,int destinationCode, int unitCode){
        if(frameFromQueue!=null&& frameFromQueue.length>3&&frameFromQueue[0]==frameHead&&frameFromQueue[1]==destinationCode){
           if(frameFromQueue[2]==unitCode){
               return frameFromQueue[3];//功能码
           }
        }
        return -1;
    }

    /**
     * 从一个完整帧中取出数据码（6个）
     * @param frameWithoutSpecialChar 已经去除转义字符的完整帧
     * @return 返回数据码
     */
    public int[] getFirstUnitFuncOneDataCode(int[] frameWithoutSpecialChar){
        if(frameWithoutSpecialChar==null||frameWithoutSpecialChar.length!=13){
            Log.d(TAG,"获取到的完整帧（已处理转义符）为空或者帧大小不是13位");
            return null;
        }
        if(frameWithoutSpecialChar[0]!=0xfd||frameWithoutSpecialChar[1]!=0x88||
                frameWithoutSpecialChar[2]!=0x01||frameWithoutSpecialChar[3]!=0x01){
            Log.d(TAG,"获取到的帧头不是fd 88 01 01，不是我需要的数据码");
            return null;
        }
        int[] dataCode=new int[6];
       /* for(int i=4;i<10;i++){
            dataCode[i-4]=frameWithoutSpecialChar[i];
        }*/
        System.arraycopy(frameWithoutSpecialChar,4,dataCode,0,6);
        return dataCode;
    }

    /**
     * 分析单元一功能码为一的数据码
     * @param data 数据码
     */
    public void analyzeFirstUnitFuncOne(int[] data){
        if(data==null||data.length!=6){
            Log.e(TAG, "错误：analyzeFirstUnitFirstFunc方法里的byte[]不是6位数据码");
            return;
        }
        int num=data[0]&0x7f;//车速
        int direction=data[0]&0x80;//为1为前进
        if(num>0.1){
            if(direction==0){
                num=-num;
                carProperties.setCarDirection(-1);
            }else {
                carProperties.setCarDirection(1);
            }
        }else {
            carProperties.setCarDirection(0);
        }
        carProperties.setCarVelocity(num);
        carProperties.setEngineSpeed(data[1]*60);
        num=data[2];
        carProperties.setEngineState((num&0x8)==0?1:0);
        carProperties.setCarGear(num&0x7);
        num=data[3];
        carProperties.setSbOnSeat((num&0x1)==1?1:0);
        carProperties.setCarHeadState( (num&0x2)==2?1:0);
        carProperties.setCarTailState((num&0x4)==4?1:0);
        carProperties.setBrakePressure((num&0x8)==0x8?1:0);
        carProperties.setLifeBelt((num&0x10)==0x10?1:0);
        num=data[4];
        carProperties.setFogLamp((num&0x1)==0?1:0);
        carProperties.setWidthLamp((num&0x2)==0?1:0);
        carProperties.setHandBrake((num&0x4)==0x4?1:0);
        carProperties.setFootBrake((num&0x8)==0?1:0);
        carProperties.setCarDoor((num&0x10)==0x10?1:0);
        carProperties.setIgnitionSwitch((num&0x20)==0?1:0);
        num=(data[4]>>6)&3;
        switch (num){
            case 0:
                carProperties.setHighBeam(1);
                carProperties.setLowBeam(1);
                break;
            case 1:
                carProperties.setHighBeam(1);
                carProperties.setLowBeam(0);
                break;
            case 2:
                carProperties.setHighBeam(0);
                carProperties.setLowBeam(1);
                break;
            case 3:
                carProperties.setHighBeam(0);
                carProperties.setLowBeam(0);
                break;
        }
        num=data[5];
        carProperties.setLeftTurn((num&0x1)==0x1?1:0);
        carProperties.setRightTurn((num&0x2)==0x2?1:0);
        carProperties.setCarHorn((num&0x4)==0x4?1:0);
        carProperties.setViceBrake((num&0x8)==0?1:0);
        num=data[5]>>4;
        carProperties.setWarningLamp((num&0x1)==0x1?1:0);
        carProperties.setCarClutch((num&0x2)==0?1:0);
        carProperties.setCarAccelerator((num&0x4)==0x4?1:0);
    }

    /**
     * 处理转义字符
     * @param special 转义字符
     * @return 返回处理过的转义字符
     */
    public int handleSpecialHex(int special)  {
        switch (special){
            case 0x00:
                return 0xfb;
            case 0x01:
                return 0xfc;
            case 0x02:
                return 0xfd;
            case 0x03:
                return 0xfe;
            case 0x04:
                return 0xff;
            default:
                throw new IllegalArgumentException("handleSpecialHex字符码不在定义的转义字符中");
        }
    }



}
