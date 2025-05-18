package com.shadcn.courseservice.service.impl;

import com.shadcn.courseservice.dto.request.course.BaseCourseCreationRequest;
import com.shadcn.courseservice.dto.request.course.CourseCreationRequest;
import com.shadcn.courseservice.dto.request.course.UpdateConstraintCourseRequest;
import com.shadcn.courseservice.dto.request.course.UpdateCourseInformationRequest;
import com.shadcn.courseservice.dto.response.PageResponse;
import com.shadcn.courseservice.dto.response.course.BaseCourseResponse;
import com.shadcn.courseservice.dto.response.course.CourseResponse;
import com.shadcn.courseservice.dto.response.teacher.TeacherInformationDTO;
import com.shadcn.courseservice.dto.response.teacher.TeacherProfileResponse;
import com.shadcn.courseservice.dto.response.user.UserProfileResponse;
import com.shadcn.courseservice.entity.*;
import com.shadcn.courseservice.exception.AppException;
import com.shadcn.courseservice.exception.ErrorCode;
import com.shadcn.courseservice.mapper.CourseMapper;
import com.shadcn.courseservice.mapper.TeacherMapper;
import com.shadcn.courseservice.repository.*;
import com.shadcn.courseservice.repository.httpClient.IdentityClient;
import com.shadcn.courseservice.repository.httpClient.ProfileClient;
import com.shadcn.courseservice.service.ICourseService;
import com.shadcn.courseservice.service.IFileUploadService;
import com.shadcn.courseservice.service.IProfileService;
import com.shadcn.courseservice.util.ConverToPaginationResponse;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CourseService implements ICourseService {
    DepartmentRepository departmentRepository;
    CourseRepository courseRepository;
    SemesterRepository semesterRepository;
    CourseMapper courseMapper;
    IFileUploadService fileUploadService;
    IProfileService profileService;
    ProfileClient profileClient;
    IdentityClient identityClient;
    BaseCourseRepository baseCourseRepository;
    StudentReferenceRepository studentReferenceRepository;
    TeacherReferenceRepository teacherReferenceRepository;
    TeacherMapper teacherMapper;

    @Override
    @Transactional
    public void addStudentIntoCourse(String departmentId, String courseId, List<String> studentIds) {
        Department department = getDepartment(Long.valueOf(departmentId));
        Course course = getCourse(department, courseId);

        for (String id : studentIds) {
            StudentReference studentReference = studentReferenceRepository
                    .findByStudentId(Long.valueOf(id))
                    .orElseThrow(() -> new AppException(ErrorCode.STUDENT_NOT_FOUND));
            if (!course.getStudentReferences().contains(studentReference)) {
                course.getStudentReferences().add(studentReference);
            }
        }
        departmentRepository.save(department);
        courseRepository.save(course);
    }

    @Override
    @Transactional
    public void removeStudentFromCourse(String departmentId, String courseId, List<String> studentIds) {
        Department department = getDepartment(Long.valueOf(departmentId));
        Course course = getCourse(department, courseId);

        for (String id : studentIds) {
            StudentReference studentReference = studentReferenceRepository
                    .findByStudentId(Long.valueOf(id))
                    .orElseThrow(() -> new AppException(ErrorCode.STUDENT_NOT_FOUND));
            course.getStudentReferences().remove(studentReference);
        }

        departmentRepository.save(department);
        courseRepository.save(course);
    }

    @Override
    @Transactional
    public void addTeacherIntoCourse(String departmentId, String courseId, List<String> teacherIds) {
        Department department = getDepartment(Long.valueOf(departmentId));
        Course course = getCourse(department, courseId);

        for (String id : teacherIds) {
            TeacherReference teacherReference = teacherReferenceRepository
                    .findByTeacherId(Long.valueOf(id))
                    .orElseThrow(() -> new AppException(ErrorCode.TEACHER_NOT_FOUND));
            if (!course.getTeacherReferences().contains(teacherReference)) {
                course.getTeacherReferences().add(teacherReference);
            }
        }
        departmentRepository.save(department);
        courseRepository.save(course);
    }

    @Override
    @Transactional
    public void removeTeacherFromCourse(String departmentId, String courseId, List<String> teacherIds) {
        Department department = getDepartment(Long.valueOf(departmentId));
        Course course = getCourse(department, courseId);

        for (String id : teacherIds) {
            TeacherReference teacherReference = teacherReferenceRepository
                    .findByTeacherId(Long.valueOf(id))
                    .orElseThrow(() -> new AppException(ErrorCode.TEACHER_NOT_FOUND));
            course.getTeacherReferences().remove(teacherReference);
        }

        departmentRepository.save(department);
        courseRepository.save(course);
    }

    @Override
    public void addSemesterIntoCourse(String departmentId, String courseId, List<String> semesterIds) {
        //                Department department = getDepartment(Long.valueOf(departmentId));
        //                Course course = getCourse(department, courseId);
        //                Semester semester;
        //
        //                for (String id : semesterIds) {
        //                    semester = getSemester(Long.valueOf(id));
        //                    if (course.getSemesters().contains(semester)) {
        //                        continue;
        //                    }
        //                    course.getSemesters().add(semester);
        //                }
        //
        //                departmentRepository.save(department);
        //                courseRepository.save(course);
    }

    @Override
    public void removeSemesterFromCourse(String departmentId, String courseId, List<String> semesterIds) {
        Department department = getDepartment(Long.valueOf(departmentId));
        Course course = getCourse(department, courseId);
        Semester semester;

        for (String id : semesterIds) {
            semester = getSemester(Long.valueOf(id));
            // course.getSemesters().remove(semester);
        }

        departmentRepository.save(department);
        courseRepository.save(course);
    }

    @Override
    @Transactional
    public void updateCourseInformation(UpdateCourseInformationRequest request, Long courseId) {
        Course course =
                courseRepository.findById(courseId).orElseThrow(() -> new AppException(ErrorCode.COURSE_NOT_FOUND));
        courseMapper.updateCourseInformation(course, request);
        courseRepository.save(course);
    }

    @Override
    public PageResponse<CourseResponse> findAllCoursesBySemesterId(Long semesterId, int current, int pageSize) {

        Pageable pageable = PageRequest.of(current - 1, pageSize);
        Page<Course> courses = courseRepository.findBySemesterId(semesterId, pageable);

        return ConverToPaginationResponse.toPageResponse(courses, courseMapper::toCourseResponse, current);
    }

    @Override
    @Transactional
    public void updateCourseConstraint(Long id, UpdateConstraintCourseRequest request) {
        Course course = courseRepository
                .findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.COURSE_NOT_FOUND));
        course.setMaxStudents(request.getMaxStudents());
        course.setNumsOfTimetable(request.getNumsOfTimetable());
        courseRepository.save(course);
    }

    @Override
    public CourseResponse getCourseById(String courseId) {
        Course course = courseRepository
                .findById(Long.valueOf(courseId))
                .orElseThrow(() -> new AppException(ErrorCode.COURSE_NOT_FOUND));
        TeacherProfileResponse teacherProfileResponse = identityClient
                .getTeacherProfileById(course.getTeacherReferences().get(0).getTeacherId())
                .getResult();

        TeacherReference teacherReference = teacherReferenceRepository
                .findByTeacherId(course.getTeacherReferences().get(0).getTeacherId())
                .orElseThrow(() -> new AppException(ErrorCode.TEACHER_NOT_FOUND));

        TeacherInformationDTO teacherInfo = teacherMapper.toTeacherInfo(teacherProfileResponse, teacherReference);
        return courseMapper.toCourseResponseDetail(course, teacherInfo);
    }

    @Override
    public List<CourseResponse> getCoursesOfCurrentTeacherBySemesterId(String semesterId) {
        UserProfileResponse userProfile = identityClient.getCurrentUserProfile().getResult();
        log.info("userProfile: {}", userProfile);
        Semester semester = semesterRepository
                .findById(Long.valueOf(semesterId))
                .orElseThrow(() -> new AppException(ErrorCode.SEMESTER_NOT_FOUND));
        TeacherReference teacherReference = teacherReferenceRepository
                .findByTeacherId(Long.valueOf(userProfile.getId()))
                .orElseThrow(() -> new AppException(ErrorCode.TEACHER_NOT_FOUND));
        List<Course> courses = courseRepository.findByTeacherReferencesAndSemester(teacherReference, semester);
        return courseMapper.toCourseResponseList(courses);
    }

    @Override
    public PageResponse<UserProfileResponse> getAllStudentsInCourseByIds(String courseId, int current, int pageSize) {

        Pageable pageable = PageRequest.of(current - 1, pageSize);
        Course course = courseRepository
                .getCourseById(Long.valueOf(courseId))
                .orElseThrow(() -> new AppException(ErrorCode.COURSE_NOT_FOUND));

        List<StudentReference> studentReferences = studentReferenceRepository.findAllByCourses(List.of(course));
        List<Long> studentIds =
                studentReferences.stream().map(StudentReference::getStudentId).collect(Collectors.toList());

        List<UserProfileResponse> studentProfiles =
                identityClient.getUserProfileResponses(studentIds).getResult();
        Page<UserProfileResponse> responses = new PageImpl<>(studentProfiles, pageable, studentProfiles.size());

        return ConverToPaginationResponse.toPageResponse(responses, Function.identity(), current);
    }

    @Override
    public PageResponse<UserProfileResponse> getAllTeachersInCourseByIds(String courseId, int current, int pageSize) {

        Pageable pageable = PageRequest.of(current - 1, pageSize);
        Course course = courseRepository
                .getCourseById(Long.valueOf(courseId))
                .orElseThrow(() -> new AppException(ErrorCode.COURSE_NOT_FOUND));

        List<TeacherReference> teacherReferences = teacherReferenceRepository.findAllByCourses(List.of(course));
        List<Long> teacherIds =
                teacherReferences.stream().map(TeacherReference::getTeacherId).collect(Collectors.toList());

        List<UserProfileResponse> teacherProfiles =
                identityClient.getUserProfileResponses(teacherIds).getResult();
        Page<UserProfileResponse> responses = new PageImpl<>(teacherProfiles, pageable, teacherProfiles.size());

        return ConverToPaginationResponse.toPageResponse(responses, Function.identity(), current);
    }

    @Override
    @Transactional
    public void uploadCourseImage(String departmentId, String courseId, MultipartFile image) {
        Course course = getCourse(getDepartment(Long.valueOf(departmentId)), courseId);

        String imageUri = fileUploadService.uploadFileIfPresent(image);

        if (imageUri != null) {
            course.setImageUri(imageUri);
            courseRepository.save(course);
        } else {
            throw new AppException(ErrorCode.IMAGE_UPLOAD_FAILED);
        }

        courseRepository.save(course);
    }

    @Override
    public void uploadCourseFile(String departmentId, String courseId, List<MultipartFile> files) {
        Course course = getCourse(getDepartment(Long.valueOf(departmentId)), courseId);

        for (MultipartFile file : files) {
            String fileUri = fileUploadService.uploadFileIfPresent(file);
            if (fileUri == null) {
                throw new AppException(ErrorCode.FILE_UPLOAD_FAILED);
            }
            CourseFile courseFile = CourseFile.builder()
                    .course(course)
                    .name(file.getOriginalFilename())
                    .type(file.getContentType())
                    .url(fileUri)
                    .build();
            course.getFiles().add(courseFile);
        }

        courseRepository.save(course);
    }

    @Override
    public PageResponse<BaseCourseResponse> getAllCourses(Integer current, Integer pageSize) {
        Pageable pageable = PageRequest.of(current - 1, pageSize);
        Page<BaseCourse> baseCourses = baseCourseRepository.findAll(pageable);
        return ConverToPaginationResponse.toPageResponse(baseCourses, courseMapper::toBaseCourseResponse, current);
    }

    @Override
    public void createBaseCourse(BaseCourseCreationRequest request) {
        if (baseCourseRepository.existsByCode(request.getCode())) {
            throw new AppException(ErrorCode.BASE_COURSE_EXISTED);
        }

        BaseCourse baseCourse = courseMapper.toBaseCourse(request);
        baseCourseRepository.save(baseCourse);
    }

    @Override
    public void createNewCourseFromBaseCourseInSemester(CourseCreationRequest request) {
        if (semesterRepository.existsCourseInSemester(request.getBaseCourseId(), request.getSemesterId())) {
            throw new AppException(ErrorCode.COURSE_EXISTED_IN_SEMESTER);
        }

        BaseCourse baseCourse = baseCourseRepository
                .findById(request.getBaseCourseId())
                .orElseThrow(() -> new AppException(ErrorCode.RESOURCE_NOT_FOUND));

        Course course = courseMapper.toCourse(request, baseCourse);
        courseRepository.save(course);
    }

    @Override
    public void removeCourseInstanceFromSemester(String courseId, String semesterId) {
        Course course = courseRepository
                .findById(Long.valueOf(courseId))
                .orElseThrow(() -> new AppException(ErrorCode.COURSE_NOT_FOUND));

        Semester semester = semesterRepository
                .findById(Long.valueOf(semesterId))
                .orElseThrow(() -> new AppException(ErrorCode.SEMESTER_NOT_FOUND));

        semester.getCourses().remove(course);
        courseRepository.save(course);
    }

    public Department getDepartment(Long departmentId) {
        return departmentRepository
                .findById(departmentId)
                .orElseThrow(() -> new AppException(ErrorCode.DEPARTMENT_NOT_FOUND));
    }

    public Course getCourse(Department department, String courseId) {
        return department.getCourses().stream()
                .filter(c -> c.getId().equals(Long.valueOf(courseId)))
                .findFirst()
                .orElseThrow(() -> new AppException(ErrorCode.COURSE_NOT_FOUND));
    }

    Semester getSemester(Long semesterId) {
        return semesterRepository
                .findById(semesterId)
                .orElseThrow(() -> new AppException(ErrorCode.SEMESTER_NOT_FOUND));
    }

    //    void registerCourseForStudent(String studenId, List<CourseId> courseIds) {
    //        SubjectRegistration registration;
    //
    //
    //        for(CourseId courseId : courseIds) {
    //            Course course = courseRepository.findById(courseId.getId()).orElseThrow(() -> new
    // AppException(ErrorCode.COURSE_NOT_FOUND));
    //            registration = new Registration();
    //            registration.setCourse(course);
    //            registration.addStudent(studenId);
    //        }
    //    }
}
