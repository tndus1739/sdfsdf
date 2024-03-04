package com.himedia.hicinema.reservation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.google.gson.JsonObject;
import com.himedia.hicinema.movie.Movie;
import com.himedia.hicinema.movie.MovieService;
import com.himedia.hicinema.movie.loc.Location;
import com.himedia.hicinema.movie.loc.LocationService;
import com.himedia.hicinema.movie.schedule.Schedule;
import com.himedia.hicinema.movie.schedule.ScheduleService;
import com.himedia.hicinema.movie.theater.Theater;
import com.himedia.hicinema.movie.theater.TheaterService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/reservation")
public class ReservationController {
    private final LocationService locationService;
    private final TheaterService theaterService;
    private final MovieService movieService;
    private final ScheduleService scheduleService;

    @GetMapping("/ticketing")
    public String reservationPage(Model model) {
        List<Location> locations = locationService.getList("O");
        List<Theater> theaters = theaterService.getList("O");
        List<Movie> movies = movieService.getMovieList("O");

        List<String> img_arr = new ArrayList<>();
        img_arr.add("/img_nsy/aespa_19207742.jpg");
        img_arr.add("/img_nsy/BabyShark.jpg");
        img_arr.add("/img_nsy/IfOnly_1920774.jpg");

        model.addAttribute("images", img_arr);
        model.addAttribute("title", "예매");
        model.addAttribute("locations", locations);
        model.addAttribute("theaters", theaters);
        model.addAttribute("movies", movies);
        return "user/ticketing";
    }

    @GetMapping("/ticketing/get_theaters")
    @ResponseBody
    public ResponseEntity<String> getTheaterList(String loc_id) {
        JsonObject jo = new JsonObject();
        try {
            Optional<Location> opt = locationService.getLocation(Long.valueOf(loc_id), "O");
            if(opt.isPresent()) {
                Location loc = opt.get();
                List<Theater> theaters = theaterService.getTheaterList(loc, "O");
                ObjectMapper om = new ObjectMapper();
                om.registerModule(new JavaTimeModule());
                String json = om.writeValueAsString(theaters);
                jo.addProperty("theaters", json);
                jo.addProperty("msg", "success");
            } else {
                jo.addProperty("msg", "error");
            }
        }catch (Exception e) {
            e.printStackTrace();
            jo.addProperty("msg", "error");
        }
        return new ResponseEntity<>(jo.toString(), HttpStatus.OK);
    }

    @GetMapping("/ticketing/get_schedule")
    @ResponseBody
    public ResponseEntity<String> get_schedule(String theaterId, String movieCd, @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime screeningDate) {
        JsonObject jo = new JsonObject();
        ZoneId zoneId = ZoneId.of("Asia/Seoul"); // 또는 원하는 시간대로 설정
        ZonedDateTime zonedDateTime = screeningDate.atZone(ZoneOffset.UTC).withZoneSameInstant(zoneId);
        LocalDateTime scd = zonedDateTime.toLocalDateTime();
        log.info("=============================================");
        System.out.println(theaterId);
        System.out.println(movieCd);
        System.out.println(scd);
        log.info("=============================================");
        try {
//            Movie movie = movieService.getMovie(movieCd, "O");
//            Theater theater = theaterService.getDetail(Long.valueOf(theaterId));
            List<Map<String, Object>> schedule = scheduleService.getUserPageSchedule(movieCd, scd, Long.valueOf(theaterId));
            ObjectMapper om = new ObjectMapper();
            om.registerModule(new JavaTimeModule());
            String json = om.writeValueAsString(schedule);
            jo.addProperty("msg", "success");
            jo.addProperty("schedules", json);
        } catch (Exception e){
            e.printStackTrace();
            jo.addProperty("msg", "error");
        }

        return new ResponseEntity<>(jo.toString(), HttpStatus.OK);
    }

    @GetMapping("/ticketing/order")
    public String orderPage(Model model) {
        model.addAttribute("title", "예약");
        return "user/ticketing_order";
    }

    @GetMapping("/ticketing/seat")
    public String seatPage(Model model) {
        model.addAttribute("title", "인원/좌석 선택");

        return "user/seat";
    }

}
