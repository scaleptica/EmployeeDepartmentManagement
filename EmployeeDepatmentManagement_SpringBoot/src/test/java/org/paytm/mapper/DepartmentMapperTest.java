package org.paytm.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.TypeMap;
import org.paytm.dto.AddressDto;
import org.paytm.dto.DepartmentDto;
import org.paytm.entities.Address;
import org.paytm.entities.Department;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigInteger;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class DepartmentMapperTest {

    @Mock
    private TypeMap<DepartmentDto, Department> typeMapDTO;

    @Mock
    private TypeMap<Department, DepartmentDto> typeMapDTO2;

    @InjectMocks
    private DepartmentMapper departmentMapper;

    @BeforeEach
    void setup(){



    }

    @Test
    public void fromDtoToEntityTest_success() {

        Department REC1 = new Department();
        LocalDateTime created_at1 = LocalDateTime.parse("2000-04-04T10:20");
        LocalDateTime updated_at1 = LocalDateTime.parse("2001-06-04T09:20");
        REC1.setId(BigInteger.valueOf(1));
        REC1.setName("Research and Development");
        REC1.setRemarks("Scientists");
        REC1.setCreatedAt(created_at1);
        REC1.setUpdatedAt(updated_at1);

        DepartmentDto REC1dto = new DepartmentDto();
        REC1dto.setId(BigInteger.valueOf(1));
        REC1dto.setName("Research and Development");
        REC1dto.setRemarks("Scientists");
        REC1dto.setCreatedAt(created_at1);
        REC1dto.setUpdatedAt(updated_at1);

        when(this.typeMapDTO.map(REC1dto)).thenReturn(REC1);

        assertEquals(REC1, this.departmentMapper.fromDtoToEntity(REC1dto));
    }

    @Test
    public void fromEntityToDtoTest_success() {

        Department REC1 = new Department();
        LocalDateTime created_at1 = LocalDateTime.parse("2000-04-04T10:20");
        LocalDateTime updated_at1 = LocalDateTime.parse("2001-06-04T09:20");
        REC1.setId(BigInteger.valueOf(1));
        REC1.setName("Research and Development");
        REC1.setRemarks("Scientists");
        REC1.setCreatedAt(created_at1);
        REC1.setUpdatedAt(updated_at1);

        DepartmentDto REC1dto = new DepartmentDto();
        REC1dto.setId(BigInteger.valueOf(1));
        REC1dto.setName("Research and Development");
        REC1dto.setRemarks("Scientists");
        REC1dto.setCreatedAt(created_at1);
        REC1dto.setUpdatedAt(updated_at1);

        when(this.typeMapDTO2.map(REC1)).thenReturn(REC1dto);

        assertEquals(REC1dto, this.departmentMapper.fromEntityToDto(REC1));

    }
}
