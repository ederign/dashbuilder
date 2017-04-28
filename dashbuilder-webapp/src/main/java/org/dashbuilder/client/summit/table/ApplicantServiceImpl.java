package org.dashbuilder.client.summit.table;

import com.google.gwt.core.client.GWT;
//porcelli
//import org.drools.workbench.shared.Applicant;
//import org.drools.workbench.shared.ApplicantService;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;
import java.util.stream.Collectors;

@ApplicationScoped
public class ApplicantServiceImpl implements ApplicantService {

    List<Applicant> listFront = new ArrayList<>();
    List<Applicant> listBack = new ArrayList<>();

    @PostConstruct
    public void setup() {


        listBack.add( createApplicant( "Alex", 35, "US", TypeInsert.ALL ) );
        listFront.add( createApplicant( "Alex", 35, "US", TypeInsert.ALL ) );
        listBack.add( createApplicant( "Michael", 36, "GB", TypeInsert.BACKEND_ALL ) );
        listFront.add( createApplicant( "Mauricio", 30, "GB", TypeInsert.FRONT ) );
        listBack.add( createApplicant( "Eder", 32, "Brazil", TypeInsert.BACKEND_DROOLS ) );
        listFront.add( createApplicant( "Paulo", 25, "Brazil", TypeInsert.FRONT_JS ) );

//        dataInsert.add( "Guilherme", 24, TypeInsert.JS ));
        listBack.add( createApplicant( "Kris", 55, "BE", TypeInsert.ALL ) );
        listFront.add( createApplicant( "Kris", 55, "BE", TypeInsert.ALL ) );

        listBack.add( createApplicant( "Geoffrey", 35, "BE", TypeInsert.ALL ) );
        listFront.add( createApplicant( "Geoffrey", 35, "BE", TypeInsert.ALL ) );

        listBack.add( createApplicant("Mark", 36, "GB", TypeInsert.BACKEND_DROOLS ));
        listFront.add( createApplicant("Joe", 30, "US", TypeInsert.FRONT ));
        listFront.add( createApplicant("Sarah", 32, "US", TypeInsert.FRONT_JS ));
        listBack.add( createApplicant("Edson", 54, "Canada", TypeInsert.BACKEND_JAVA ));


    }

    private Applicant createApplicant( String name, int age, String country, TypeInsert typeInsert ) {
        Applicant applicant = new Applicant();
        applicant.setAge( age );
        applicant.setCountry( country );
        applicant.setName( name );
        if ( typeInsert == TypeInsert.FRONT ||
                typeInsert == TypeInsert.FRONT_JS ||
                typeInsert == TypeInsert.ALL ) {
            applicant.setHasCSSSkill( true );
            applicant.setHasHTMLSkill( true );
        }
        if ( typeInsert == TypeInsert.FRONT_JS ||
                typeInsert == TypeInsert.JS ||
                typeInsert == TypeInsert.ALL ) {
            applicant.setHasJavaScriptSkill( true );
        }
        if ( typeInsert == TypeInsert.BACKEND_JAVA ||
                typeInsert == TypeInsert.BACKEND_ALL ||
                typeInsert == TypeInsert.BACKEND_DROOLS ||
                typeInsert == TypeInsert.ALL ) {
            applicant.setHasJavaSkill( true );
        }
        if ( typeInsert == TypeInsert.BACKEND_JAVA ||
                typeInsert == TypeInsert.BACKEND_ALL ||
                typeInsert == TypeInsert.ALL ) {
            applicant.setHasJBPMSkill( true );
        }
        if ( typeInsert == TypeInsert.BACKEND_DROOLS ||
                typeInsert == TypeInsert.BACKEND_ALL ||
                typeInsert == TypeInsert.ALL ) {
            applicant.setHasDroolsSkill( true );
        }
        return applicant;
    }


    @Override
    public List<Applicant> filterApplicants( String drl ) {
        List<Applicant> list = new ArrayList<>();
        GWT.log(drl);
        if ( drl.toUpperCase().contains( "FRONT" ) ) {
            list.addAll( listFront );
        } else if ( drl.toUpperCase().contains( "BACK" ) ) {
            list.addAll( listBack );
        }
        else if (drl.isEmpty()){
            List<Applicant> tempList = new ArrayList<>();
            tempList.addAll(listBack);
            tempList.addAll(listFront);
            Collection<Applicant> nonDuplicatedApplicants = tempList.stream()
                    .<Map<String, Applicant>> collect(HashMap::new, (m, e)->m.put(e.getName(), e), Map::putAll)
                    .values();
            list.addAll(nonDuplicatedApplicants);

        }
        return list;
    }

    public enum TypeInsert {
        ALL, FRONT, FRONT_JS, JS, BACKEND_JAVA, BACKEND_ALL, BACKEND_DROOLS
    }
}
