package com.tlias.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import com.tlias.pojo.Emp;

import com.tlias.pojo.EmpQueryParam;
@Mapper
public interface EmpMapper {

    //----------------------原始-------------------------------
    //查询总记录数

    //@Select("SELECT COUNT(*) FROM emp AS e LEFT OUTER JOIN dept AS d ON  e.dept_id = d.id;")
    //Long queryTotalPage();

    //返回列表
    //@Select("SELECT e.*,d.name AS deptName FROM emp AS e LEFT OUTER JOIN dept AS d ON e.dept_id = d.id LIMIT #{currentpage},#{pageSize};")
    //List<Emp> queryEveryPageEmps(@Param("currentpage") Integer cuurentpage,@Param("pageSize") Integer pageSize);

    //@Select("SELECT e.*,d.name AS deptName FROM emp AS e LEFT OUTER JOIN dept AS d ON e.dept_id = d.id ORDER BY update_time desc")
    List<Emp> queryEveryPageEmps(EmpQueryParam empQueryParam);

    
    void addEmpinfo(Emp empInfo);

    void deleteEmpinfos(List<Integer> ids);


    void modifyEmpinfo(Emp empInfo);

    Emp queryEmpinfoById(Integer id);

    List<Emp> queryAllEmpinfo();
}
