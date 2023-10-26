package HTQ.BDS.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@MappedSuperclass
public class BaseEntity {

    @Column(name = "create_at")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createAt;
    @Column(name = "update_at")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateAt;
    @Column(name = "last_login_at")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime lastLoginAt;
    @Transient
    private boolean onlyUpdateLastLogin = false;

    /**
     * @PrePersist => đánh dấu pt onCreate() sẽ đưc thực thi trước khi Entity lưu vào db.||
     * @PreUpdate => đánh dấu pt onUpdate() sẽ đươc thực thi trước khi cập nhật vào db
     *
     * */
    @PrePersist
    protected void onCreate(){
        createAt = LocalDateTime.now(); // đặt thời điểm hiện tại
        updateAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate(){
        if (!onlyUpdateLastLogin) {
            updateAt = LocalDateTime.now();
        }
        onlyUpdateLastLogin = false;
    }
}
