package br.company.config;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import org.springframework.boot.context.properties.ConfigurationPropertiesBinding;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;


@Component
@ConfigurationPropertiesBinding
public class ZonedDateTimeConverter implements Converter<String, ZonedDateTime> {

    @Override
    public ZonedDateTime convert(String source) {
        try {
            if (source == null) {
                return null;
            }
            
            if (source.contains(":") == false) {

                LocalDate ld = LocalDate.parse(source, DateTimeFormatter.ofPattern("yyyy-MM-dd"));

                ZonedDateTime atStartOfDay = ld.atStartOfDay(ZoneId.systemDefault());

                return atStartOfDay;
            } else {
                return ZonedDateTime.parse(source);
            }

        } catch (Exception ex) {
            //ex.printStackTrace();
        }

        return null;
    }

}
