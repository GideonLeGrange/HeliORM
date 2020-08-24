package test.persons;

import com.heliorm.annotation.Pojo;
import com.heliorm.annotation.PrimaryKey;

/**
 *
 * @author gideon
 */
@Pojo
public class Company  {

    @PrimaryKey
    private Long companyNumber;
    private String name;

    public Long getCompanyNumber() {
        return companyNumber;
    }

    public void setCompanyNumber(Long companyNumber) {
        this.companyNumber = companyNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Company{" + "companyNumber=" + companyNumber + ", name=" + name + '}';
    }

}
