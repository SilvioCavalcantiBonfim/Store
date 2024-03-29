package com.microsservicos.shoppingapi.unit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.List;

import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.microsservicos.dto.ItemDto;
import com.microsservicos.dto.ReportDto;
import com.microsservicos.dto.ShopOutputDto;
import com.microsservicos.shoppingapi.model.Item;
import com.microsservicos.shoppingapi.model.Shop;
import com.microsservicos.shoppingapi.repository.ShopRepository;
import com.microsservicos.shoppingapi.service.impl.ReportServiceImpl;

@ExtendWith(MockitoExtension.class)
public class ReportServiceImplTest {

  @Mock
  private ShopRepository shopRepository;

  @InjectMocks
  private ReportServiceImpl reportService;

  private static Shop ORDER_1 = new Shop();
  private static Item ITEM_1 = new Item();
  private static Item ITEM_2 = new Item();
  private static ItemDto ITEM_1_DTO = new ItemDto("XYZ000", 10.0f, 1);
  private static ItemDto ITEM_2_DTO = new ItemDto("XYZ001", 5.5f, 2);

    private static LocalDateTime date;

  @BeforeEach
  private void setup() {
    date = LocalDateTime.now();

    ITEM_1.setProductIdentifier("XYZ000");
    ITEM_1.setPrice(10.0f);
    ITEM_1.setAmount(1);
    
    ITEM_2.setProductIdentifier("XYZ001");
    ITEM_2.setPrice(5.5f);
    ITEM_2.setAmount(2);
    
    ORDER_1.setId(1L);
    ORDER_1.setUserIdentifier("ABC123");
    ORDER_1.setTotal(10.0);
    ORDER_1.setDate(date);
    ORDER_1.setItens(List.of(ITEM_1, ITEM_2));
  }

  @Test
  public void getShopByFiltersWithAllArgsNotNull(){
    when(shopRepository.retrieveShopsByDateAndTotal(any(), any(), any())).thenReturn(List.of(ORDER_1));

    List<ShopOutputDto> result = reportService.retrieveShopsByDateAndMinimumTotal(date.toLocalDate(), date.toLocalDate(), 100.0f);

    assertThat(result).extracting("userIdentifier", "total", "date", "itens")
    .contains(new Tuple("ABC123",10.0,date, List.of(ITEM_1_DTO, ITEM_2_DTO)));
  }

  @Test
  public void getShopByFiltersWithStartNull(){
    assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> reportService.retrieveShopsByDateAndMinimumTotal(null, date.toLocalDate(), 100.0f));
  }

  @Test
  public void getShopByFiltersWithEndNull(){
    when(shopRepository.retrieveShopsByDateAndTotal(any(), any(), any())).thenReturn(List.of(ORDER_1));

    List<ShopOutputDto> result = reportService.retrieveShopsByDateAndMinimumTotal(date.toLocalDate(), null, 100.0f);

    assertThat(result).extracting("userIdentifier", "total", "date", "itens")
    .contains(new Tuple("ABC123",10.0,date, List.of(ITEM_1_DTO, ITEM_2_DTO)));
  }

  @Test
  public void getShopByFiltersWithMinNull(){
    when(shopRepository.retrieveShopsByDateAndTotal(any(), any(), any())).thenReturn(List.of(ORDER_1));

    List<ShopOutputDto> result = reportService.retrieveShopsByDateAndMinimumTotal(date.toLocalDate(), date.toLocalDate(), null);

    assertThat(result).extracting("userIdentifier", "total", "date", "itens")
    .contains(new Tuple("ABC123",10.0,date, List.of(ITEM_1_DTO, ITEM_2_DTO)));
  }

  @Test
  public void getReportByDateWithAllArgsNotNull(){
    ReportDto expected = new ReportDto(10L, 100.0, 100.0);

    when(shopRepository.generateReportByDate(any(), any())).thenReturn(expected);

    ReportDto result = reportService.generateReportByDateRange(date.toLocalDate(), date.toLocalDate());

    assertThat(result).isEqualTo(expected);
  }

  @Test
  public void getReportByDateWithStartNull(){
    assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> reportService.generateReportByDateRange(null, date.toLocalDate()));
  }

  @Test
  public void getReportByDateWithEndNull(){
    ReportDto expected = new ReportDto(10L, 100.0, 100.0);

    when(shopRepository.generateReportByDate(any(), any())).thenReturn(expected);

    ReportDto result = reportService.generateReportByDateRange(date.toLocalDate(), null);

    assertThat(result).isEqualTo(expected);
  }
}
