package test.persons;

import net.legrange.orm.annotation.Pojo;
import net.legrange.orm.annotation.PrimaryKey;

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