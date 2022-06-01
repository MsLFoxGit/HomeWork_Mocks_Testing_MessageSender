import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.mockito.Mockito;
import ru.netology.entity.Country;
import ru.netology.entity.Location;
import ru.netology.geo.GeoService;
import ru.netology.geo.GeoServiceImpl;
import ru.netology.i18n.LocalizationService;
import ru.netology.i18n.LocalizationServiceImpl;
import ru.netology.sender.MessageSender;
import ru.netology.sender.MessageSenderImpl;
import java.util.*;

import static org.mockito.Matchers.*;


public class Tests {
    @DisplayName("MSL: sendTest")
    @ParameterizedTest
    @CsvFileSource(resources = "/testData.csv")
    public void sendTest(String sender, String ip, Country country, String text, Country countryById){
        GeoService geoService = Mockito.mock(GeoService.class);
        Mockito.when(geoService.byIp(anyString()))
                .thenReturn(new Location(null, country, null, 0));

        LocalizationService localizationService = Mockito.mock(LocalizationService.class);
        Mockito.when(localizationService.locale(country))
                .thenReturn(text);

        Map<String,String> header = new HashMap<>();

        header.put(sender,ip);

        MessageSender messageSender = new MessageSenderImpl(geoService,localizationService);
        Assertions.assertEquals( text , messageSender.send(header));

        System.out.println();
    }
    @DisplayName("MSL: GeoServiceImpl.byIp(String ip)      Test")
    @ParameterizedTest
    @CsvFileSource(resources = "/testData.csv")
    public void byIpTest(String sender, String ip, Country country, String text, Country countryById){
        GeoService geoService = new GeoServiceImpl();
            Assertions.assertEquals( countryById, geoService.byIp(ip).getCountry());
    }

    @DisplayName("MSL: LocalizationServiceImpl.locale(Country country)  Test")
    @ParameterizedTest
    @CsvFileSource(resources = "/testData.csv")
    public void localeTest(String sender, String ip, Country country, String text, String countryById){
        LocalizationService location = new LocalizationServiceImpl();
        Assertions.assertEquals( text, location.locale(country));
    }


    @DisplayName("MSL: Location.getCountry  Test")
    @ParameterizedTest
    @CsvFileSource(resources = "/testData.csv")
    public void getCountryTest(String sender, String ip, Country country, String text, String countryById){
        Location location = new Location(null, country, null, 0);
        Assertions.assertEquals( country , location.getCountry());
    }
    @DisplayName("MSL: GeoServiceImpl.byCoordinates  Test")
    @Test
    public void byCoordinatesTest() {
        GeoService geoService = new GeoServiceImpl();
        Assertions.assertThrows(RuntimeException.class,
                    () -> geoService.byCoordinates( 33.0, 33.0));
        }
}
