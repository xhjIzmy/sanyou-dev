package com.sanyou.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.sanyou.mapper.EquipmentMapper;
import com.sanyou.mapper.IndustryDataMapper;
import com.sanyou.pojo.Equipment;
import com.sanyou.pojo.IndustryData;
import com.sanyou.pojo.vo.IndustryDataVo;
import com.sanyou.pojo.vo.TableData;
import com.sanyou.service.IndustryDataService;
import com.sanyou.utils.PagedResult;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * User: asus
 * Date: 2021/6/14
 * Time: 15:02
 * Version:V1.0
 */
@Service
public class IndustryDataServiceImpl implements IndustryDataService {

    @Autowired
    private IndustryDataMapper industryDataMapper;

    @Autowired
    private EquipmentMapper equipmentMapper;

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public List<IndustryDataVo> getPieChart(IndustryDataVo industryDataVo) {

        Example example = new Example(Equipment.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("equipNo",industryDataVo.getLineno());
        List<Equipment> equipmentList = equipmentMapper.selectByExample(example);

        if(equipmentList != null && equipmentList.size()>0){
            industryDataVo.setEquipHealthLimit(equipmentList.get(0).getEquipHealthLimit());
            industryDataVo.setEquipSubhealthLimit(equipmentList.get(0).getEquipSubhealthLimit());
            List<IndustryDataVo> result = industryDataMapper.getPieChart(industryDataVo);
            return result;
        }else {
            return new ArrayList<>();
        }
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public List<IndustryDataVo> getBarChart(IndustryDataVo industryDataVo) {

        Example example = new Example(Equipment.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("equipNo",industryDataVo.getLineno());
        List<Equipment> equipmentList = equipmentMapper.selectByExample(example);

        List<IndustryDataVo> result = new ArrayList<>();
        if(equipmentList != null && equipmentList.size()>0){
            industryDataVo.setEquipHealthLimit(equipmentList.get(0).getEquipHealthLimit());
            industryDataVo.setEquipSubhealthLimit(equipmentList.get(0).getEquipSubhealthLimit());

            Date startTime = industryDataVo.getStartTime();
            Date endTime = industryDataVo.getEndTime();
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            while(DateUtils.truncatedCompareTo(startTime,endTime, Calendar.DATE)<0){
                industryDataVo.setStartTime(startTime);

                Calendar calendar = new GregorianCalendar();
                calendar.setTime(startTime);
                calendar.add(Calendar.DATE,1);
                startTime = calendar.getTime();
                industryDataVo.setEndTime(startTime);
                List<IndustryDataVo> getList = industryDataMapper.getPieChart(industryDataVo);

                IndustryDataVo vo = new IndustryDataVo();

                String dateStr = format.format(industryDataVo.getStartTime());
                vo.setDate(dateStr);
                for (IndustryDataVo dataVo : getList) {
                    if("??????".equals(dataVo.getName())){
                        vo.setHealthValue(dataVo.getValue());
                    }else if("?????????".equals(dataVo.getName())){
                        vo.setSubHealthValue(dataVo.getValue());
                    }else if("?????????".equals(dataVo.getName())){
                        vo.setUnHealthValue(dataVo.getValue());
                    }
                }
                result.add(vo);
            }
        }
        return result;
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public List<IndustryDataVo> getLineChart(IndustryDataVo industryDataVo) {
        Example example = new Example(Equipment.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("equipNo",industryDataVo.getLineno());
        List<Equipment> equipmentList = equipmentMapper.selectByExample(example);

        List<IndustryDataVo> result = new ArrayList<>();
        if(equipmentList != null && equipmentList.size()>0){
            industryDataVo.setEquipHealthLimit(equipmentList.get(0).getEquipHealthLimit());
            industryDataVo.setEquipSubhealthLimit(equipmentList.get(0).getEquipSubhealthLimit());

            Date startTime = industryDataVo.getStartTime();
            Date endTime = industryDataVo.getEndTime();
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            while(DateUtils.truncatedCompareTo(startTime,endTime, Calendar.DATE)<0){
                industryDataVo.setStartTime(startTime);

                Calendar calendar = new GregorianCalendar();
                calendar.setTime(startTime);
                calendar.add(Calendar.DATE,1);
                startTime = calendar.getTime();
                industryDataVo.setEndTime(startTime);
                List<IndustryDataVo> getList = industryDataMapper.getPieChart(industryDataVo);

                IndustryDataVo vo = new IndustryDataVo();

                String dateStr = format.format(industryDataVo.getStartTime());
                vo.setDate(dateStr);
                for (IndustryDataVo dataVo : getList) {
                    if("??????".equals(dataVo.getName())){
                        vo.setHealthValue(dataVo.getValue());
                    }else if("?????????".equals(dataVo.getName())){
                        vo.setSubHealthValue(dataVo.getValue());
                    }else if("?????????".equals(dataVo.getName())){
                        vo.setUnHealthValue(dataVo.getValue());
                    }
                }
                result.add(vo);
            }
        }
        return result;
    }


    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public PagedResult query(IndustryDataVo industryDataVo,Integer page, Integer pageSize) {

        PageHelper.startPage(page,pageSize);
        List<IndustryData> list = industryDataMapper.query(industryDataVo);

        PageInfo<IndustryData> pageList = new PageInfo<>(list);

        PagedResult pagedResult = new PagedResult();
        pagedResult.setPage(page);
        pagedResult.setTotal(pageList.getPages());
        pagedResult.setRows(list);
        pagedResult.setRecords(pageList.getTotal());
        return pagedResult;
    }


    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public List<IndustryDataVo> getCycleLineChart(IndustryDataVo industryDataVo) {
        Example example = new Example(Equipment.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("equipNo",industryDataVo.getLineno());
        List<Equipment> equipmentList = equipmentMapper.selectByExample(example);

        if(equipmentList != null && equipmentList.size()>0){
            int equipCycle = equipmentList.get(0).getEquipCycle();
            Date startTime = industryDataVo.getStartTime();
            Date endTime = industryDataVo.getEndTime();

            industryDataVo.setEquipHealthLimit(equipmentList.get(0).getEquipHealthLimit());
            industryDataVo.setEquipSubhealthLimit(equipmentList.get(0).getEquipSubhealthLimit());

            long day = Math.abs(endTime.getTime() - startTime.getTime())/(1000 * 60 * 60 * 24) + 1;
            int cycleCount = 0;
            if(day % equipCycle == 0)
                cycleCount = (int) (day/equipCycle);
            else
                cycleCount = (int) (day/equipCycle) + 1;

            List<IndustryDataVo> result = new ArrayList<>();
            SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
            for(int i =0;i<cycleCount;i++){
                industryDataVo.setStartTime(startTime);
                Calendar calendar = new GregorianCalendar();
                calendar.setTime(startTime);
                calendar.add(Calendar.DATE,equipCycle);
                startTime = calendar.getTime();
                industryDataVo.setEndTime(startTime);
                if(i == cycleCount-1)
                    industryDataVo.setEndTime(endTime);

                List<IndustryDataVo> getList = industryDataMapper.getPieChart(industryDataVo);

                IndustryDataVo vo = new IndustryDataVo();
                String startStr = format.format(industryDataVo.getStartTime());
                //??????????????????????????????,????????????????????????0??????0???,?????????????????????????????????????????????????????????
                calendar.setTime(industryDataVo.getEndTime());
                calendar.add(Calendar.DATE,-1);
                String endStr = format.format(calendar.getTime());
                vo.setDate(startStr + "-" + endStr);

                for (IndustryDataVo dataVo : getList) {
                    if("??????".equals(dataVo.getName())){
                        vo.setHealthValue(dataVo.getValue());
                    }else if("?????????".equals(dataVo.getName())){
                        vo.setSubHealthValue(dataVo.getValue());
                    }else if("?????????".equals(dataVo.getName())){
                        vo.setUnHealthValue(dataVo.getValue());
                    }
                }
                result.add(vo);
            }
            return result;
        }
        return new ArrayList<>();
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public List<IndustryDataVo> getNormalLineChart(IndustryDataVo industryDataVo) {
        Example example = new Example(Equipment.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("equipNo",industryDataVo.getLineno());
        List<Equipment> equipmentList = equipmentMapper.selectByExample(example);

        if(equipmentList != null && equipmentList.size()>0){
            int equipCycle = equipmentList.get(0).getEquipCycle();
            int tag = 3;
            boolean flag = true;
            if(equipCycle > tag) {
                do {
                    if (equipCycle % tag == 0) {
                        equipCycle /= tag;
                        flag = false;
                    } else
                        tag++;
                } while (flag);
            }

            Date startTime = industryDataVo.getStartTime();
            Date endTime = industryDataVo.getEndTime();

            industryDataVo.setEquipHealthLimit(equipmentList.get(0).getEquipHealthLimit());
            industryDataVo.setEquipSubhealthLimit(equipmentList.get(0).getEquipSubhealthLimit());

            long day = Math.abs(endTime.getTime() - startTime.getTime())/(1000 * 60 * 60 * 24) + 1;
            int cycleCount = 0;
            if(day % equipCycle == 0)
                cycleCount = (int) (day/equipCycle);
            else
                cycleCount = (int) (day/equipCycle) + 1;

            List<IndustryDataVo> result = new ArrayList<>();
            SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
            for(int i =0;i<cycleCount;i++){
                industryDataVo.setStartTime(startTime);
                Calendar calendar = new GregorianCalendar();
                calendar.setTime(startTime);
                calendar.add(Calendar.DATE,equipCycle);
                startTime = calendar.getTime();
                industryDataVo.setEndTime(startTime);
                if(i == cycleCount-1)
                    industryDataVo.setEndTime(endTime);

                List<IndustryDataVo> getList = industryDataMapper.getPieChart(industryDataVo);

                IndustryDataVo vo = new IndustryDataVo();
                String startStr = format.format(industryDataVo.getStartTime());
                //??????????????????????????????,????????????????????????0??????0???,?????????????????????????????????????????????????????????
                calendar.setTime(industryDataVo.getEndTime());
                calendar.add(Calendar.DATE,-1);
                String endStr = format.format(calendar.getTime());
                vo.setDate(startStr + "-" + endStr);

                for (IndustryDataVo dataVo : getList) {
                    if("??????".equals(dataVo.getName())){
                        vo.setHealthValue(dataVo.getValue());
                    }else if("?????????".equals(dataVo.getName())){
                        vo.setSubHealthValue(dataVo.getValue());
                    }else if("?????????".equals(dataVo.getName())){
                        vo.setUnHealthValue(dataVo.getValue());
                    }
                }
                result.add(vo);
            }
            return result;
        }
        return new ArrayList<>();
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public List<IndustryDataVo> getNormalLineChart2(IndustryDataVo industryDataVo) {

        double max = industryDataMapper.getMax(industryDataVo);

        //??????
        int step = 0;
        List<Integer> levelNums = new ArrayList<>();
        List<String> levelNames = new ArrayList<>();
        levelNums.add(step);
        step+=5;
        while(step<max){
            levelNames.add((step-5) + "-" + (step));
            levelNums.add(step);
            step+=5;
        }
        levelNames.add((step-5) + "-" + (step));

        industryDataVo.setLevelNums(levelNums);
        industryDataVo.setLevelNames(levelNames);

        if(max == 0){
            return new ArrayList<>();
        }else{
            List<IndustryDataVo> getList = industryDataMapper.getNormalLineChart(industryDataVo);
            List<IndustryDataVo> result = new ArrayList<>();
            int id = 1;
            for (String levelName : levelNames) {
                IndustryDataVo vo = new IndustryDataVo();
                vo.setId(id++);
                vo.setName(levelName);
                IndustryDataVo vo1 = getList.stream().filter(t -> levelName.equals(t.getName())).findFirst().orElse(null);
                if(vo1 != null)
                    vo.setValue(vo1.getValue());
                else
                    vo.setValue(0);

                result.add(vo);
            }
            return result;
        }
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public List<IndustryDataVo> getNormalBarChart(IndustryDataVo industryDataVo) {
        Example example = new Example(Equipment.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("equipNo",industryDataVo.getLineno());
        List<Equipment> equipmentList = equipmentMapper.selectByExample(example);

        List<IndustryDataVo> result = new ArrayList<>();
        if(equipmentList != null && equipmentList.size()>0){
            industryDataVo.setEquipHealthLimit(equipmentList.get(0).getEquipHealthLimit());
            industryDataVo.setEquipSubhealthLimit(equipmentList.get(0).getEquipSubhealthLimit());

            Date startTime = industryDataVo.getStartTime();
            Date endTime = industryDataVo.getEndTime();
            SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");

            int equipCycle = equipmentList.get(0).getEquipCycle();
            int tag = 3;
            boolean flag = true;
            if(equipCycle > tag) {
                do {
                    if (equipCycle % tag == 0) {
                        equipCycle /= tag;
                        flag = false;
                    } else
                        tag++;
                } while (flag);
            }
            long day = Math.abs(endTime.getTime() - startTime.getTime())/(1000 * 60 * 60 * 24) + 1;
            int cycleCount = 0;
            if(day % equipCycle == 0)
                cycleCount = (int) (day/equipCycle);
            else
                cycleCount = (int) (day/equipCycle) + 1;

            for(int i =0;i<cycleCount;i++){
                industryDataVo.setStartTime(startTime);
                Calendar calendar = new GregorianCalendar();
                calendar.setTime(startTime);
                calendar.add(Calendar.DATE,equipCycle);
                startTime = calendar.getTime();
                industryDataVo.setEndTime(startTime);
                if(i == cycleCount-1)
                    industryDataVo.setEndTime(endTime);

                List<IndustryDataVo> getList = industryDataMapper.getPieChart(industryDataVo);
                IndustryDataVo vo = new IndustryDataVo();
                String startStr = format.format(industryDataVo.getStartTime());
                //??????????????????????????????,????????????????????????0??????0???,?????????????????????????????????????????????????????????
                calendar.setTime(industryDataVo.getEndTime());
                calendar.add(Calendar.DATE,-1);
                String endStr = format.format(calendar.getTime());
                vo.setDate(startStr + "-" + endStr);
                for (IndustryDataVo dataVo : getList) {
                    if("??????".equals(dataVo.getName())){
                        vo.setHealthValue(dataVo.getValue());
                    }else if("?????????".equals(dataVo.getName())){
                        vo.setSubHealthValue(dataVo.getValue());
                    }else if("?????????".equals(dataVo.getName())){
                        vo.setUnHealthValue(dataVo.getValue());
                    }
                }
                result.add(vo);
            }
        }
        return result;
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public IndustryDataVo queryData(IndustryDataVo industryDataVo, Integer page){
        PageHelper.startPage(page,1);

        List<IndustryData> list = industryDataMapper.queryData(industryDataVo);
        int count = industryDataMapper.countData(industryDataVo);

        IndustryDataVo vo = new IndustryDataVo();
        if(list != null && list.size() > 0){
            IndustryData industryData = list.get(0);
            BeanUtils.copyProperties(industryData,vo);
            vo.setPage(page);
            vo.setTotal(count);
            vo.setRecords(count*26);

            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String date = format.format(industryData.getDatatime());
            vo.setDate(date);

            List<TableData> tableDataList = new ArrayList<>();
            for(int i=1;i<=26;i++){
                try {
                    Field field = industryData.getClass().getDeclaredField("max" + i);
                    field.setAccessible(true);
                    String values = (String) field.get(industryData);
                    String[] split = values.split(",");

                    TableData tableData = new TableData();
                    tableData.setMAXN("max" + i);
                    tableData.setMAX(split[0]);
                    for (int j = 1; j <= 10; j++) {
                        Field field1 = tableData.getClass().getDeclaredField("MAX" + j);
                        field1.setAccessible(true);
                        field1.set(tableData,split[j]);
                    }
                    tableDataList.add(tableData);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            vo.setList(tableDataList);
        }

        return vo;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void deleteData(IndustryDataVo industryDataVo) {
        Example example = new Example(IndustryData.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("datatime",industryDataVo.getDatatime());
        criteria.andEqualTo("plateno",industryDataVo.getPlateno());
        criteria.andEqualTo("lineno",industryDataVo.getLineno());

        industryDataMapper.deleteByExample(example);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public List<IndustryData> queryDownloadData(IndustryDataVo industryDataVo) {
        Example example = new Example(IndustryData.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("lineno",industryDataVo.getLineno());
        criteria.andBetween("datatime",industryDataVo.getStartTime(),industryDataVo.getEndTime());

        List<IndustryData> industryData = industryDataMapper.selectByExample(example);
        return industryData;
    }
}
