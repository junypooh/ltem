package com.kt.giga.home.b2b.mapper;

import com.kt.giga.home.b2b.domain.FileInfo;
import com.kt.giga.home.b2b.entity.FileManager;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * Created by junypooh on 2017-03-03.
 * <pre>
 * com.kt.giga.home.b2b.mapper.FileInfoMapper
 *
 * Class 설명을 입력하세요.
 *
 * </pre>
 *
 * @author Kyungjun, Park
 * @see
 * @since 2017-03-03 오후 3:27
 */
@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface FileInfoMapper {

    FileInfo toFileInfo(FileManager fileManager);

    FileManager toFileManager(FileInfo fileInfo);
}
