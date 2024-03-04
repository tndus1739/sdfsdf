package com.himedia.hicinema;

import com.himedia.hicinema.slide.MainSlide;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import com.himedia.hicinema.slide.MainSlideRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.time.LocalDateTime;

import java.util.List;
import java.util.Optional;
import java.util.Queue;

@SpringBootTest
class HicinemaApplicationTests {

	@Test
	void contextLoads() {
	}

//	@Autowired
//	private MainSlideRepository mainSlideRepository;
//
//	@Test
//	void testJpa() {
//		List<MainSlide> all = this.mainSlideRepository.findAll();
//		assertEquals(2, all.size());
//
//		MainSlide m = all.get(0);
//		assertEquals("1-1", m.getImgId());
//	}


}
