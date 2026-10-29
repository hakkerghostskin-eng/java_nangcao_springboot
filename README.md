# Trà Sữa — Ứng dụng web Spring Boot

Ứng dụng web giới thiệu sản phẩm trà sữa, hiển thị danh mục, kích cỡ và topping; đồng thời cung cấp giao diện đăng ký khách hàng. Nội dung trang được render phía máy chủ bằng Thymeleaf, còn dữ liệu danh mục/sản phẩm và tài khoản được truy cập qua Spring Data JPA tới MySQL.

> README này được tổng hợp từ mã nguồn và cấu hình hiện có. Dự án hiện chưa có REST API trả JSON cho các chức năng được mô tả.

## Mục lục

- [1. Giới thiệu dự án](#1-giới-thiệu-dự-án)
- [2. Công nghệ sử dụng](#2-công-nghệ-sử-dụng)
- [3. Cấu trúc thư mục dự án](#3-cấu-trúc-thư-mục-dự-án)
- [4. Phân tích các thành phần chính](#4-phân-tích-các-thành-phần-chính)
- [5. Danh sách API và route](#5-danh-sách-api-và-route)
- [6. Cấu trúc dữ liệu và database](#6-cấu-trúc-dữ-liệu-và-database)
- [7. Hướng dẫn cài đặt và chạy dự án](#7-hướng-dẫn-cài-đặt-và-chạy-dự-án)
- [8. Ví dụ sử dụng](#8-ví-dụ-sử-dụng)
- [9. Kiểm thử](#9-kiểm-thử)
- [10. Ghi chú và hướng phát triển](#10-ghi-chú-và-hướng-phát-triển)

## 1. Giới thiệu dự án

- **Tên Maven:** `demoaaaaa` (`com.example:demoaaaaa:0.0.1-SNAPSHOT`).
- **Tên ứng dụng:** `demoaaaaa`.
- **Mục đích:** xây dựng giao diện cửa hàng trà sữa để xem danh mục và sản phẩm, xem thông tin sản phẩm cùng các lựa chọn kích cỡ/topping, và tạo tài khoản khách hàng.
- **Nghiệp vụ đang có trong code:**
  - Trang chủ đọc danh mục, sản phẩm và topping từ các repository rồi đưa dữ liệu vào template.
  - Trang chi tiết đọc một sản phẩm theo ID; nếu không có sản phẩm tương ứng, ứng dụng trả về lỗi HTTP 404. Trang cũng hiển thị danh sách kích cỡ và topping.
  - Form đăng ký kiểm tra dữ liệu khách hàng, mật khẩu tối thiểu 8 ký tự và xác nhận mật khẩu; khi hợp lệ, service tạo mã khách hàng, luôn ghi ngày sinh thành `2002-01-01`, đặt giới tính thành `KHAC` nếu không được chọn, sau đó lưu khách hàng.
  - Route đăng nhập hiện chỉ hiển thị template. Chưa có xử lý xác thực hoặc đăng nhập trong controller.

## 2. Công nghệ sử dụng

Các phiên bản dưới đây chỉ ghi phiên bản được khai báo trực tiếp trong dự án. Những dependency do Spring Boot quản lý phiên bản không có version riêng trong `pom.xml`.

| Công nghệ | Phiên bản/cấu hình | Mục đích |
|---|---|---|
| Java | 21 | Ngôn ngữ và runtime mục tiêu (`java.version`) |
| Spring Boot | 4.1.1 | Nền tảng ứng dụng, khai báo qua parent Maven |
| Spring MVC | Version do Spring Boot quản lý | Controller và xử lý request web (`spring-boot-starter-webmvc`) |
| Spring Data JPA / Hibernate | Version do Spring Boot quản lý | Truy cập dữ liệu và ánh xạ entity (`spring-boot-starter-data-jpa`) |
| Thymeleaf | Version do Spring Boot quản lý | Render trang HTML phía máy chủ |
| MySQL Connector/J | Version do Spring Boot quản lý | Kết nối MySQL, scope `runtime` |
| Jakarta Validation | Version do Spring Boot quản lý | Ràng buộc dữ liệu form (`spring-boot-starter-validation`) |
| Spring Security Crypto | Version do Spring Boot quản lý | Dependency mã hóa mật khẩu; hiện chưa được gọi trong luồng đăng ký |
| Spring Boot DevTools | Version do Spring Boot quản lý | Dependency runtime tùy chọn hỗ trợ phát triển |
| JUnit 5 và Spring Boot Test | Version do Spring Boot quản lý | Kiểm thử ứng dụng |
| Maven Wrapper | Wrapper 3.3.4; Maven phân phối 3.9.16 | Build và chạy Maven không cần cài Maven riêng |

Giao diện còn có CSS và ảnh tĩnh đặt trong `src/main/resources/static`. Không thấy cấu hình Docker, migration framework hoặc thư viện frontend riêng trong dự án.

## 3. Cấu trúc thư mục dự án

```text
project-root/
├── .mvn/
│   └── wrapper/
│       └── maven-wrapper.properties
├── mvnw
├── mvnw.cmd
├── pom.xml
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/example/demoaaaaa/
│   │   │       ├── DemoaaaaaApplication.java
│   │   │       ├── controller/
│   │   │       │   ├── Homecontroller.java
│   │   │       │   └── TaiKhoanController.java
│   │   │       ├── model/
│   │   │       │   ├── DanhMuc.java
│   │   │       │   ├── KhachHang.java
│   │   │       │   ├── SanPham.java
│   │   │       │   ├── Size.java
│   │   │       │   └── Topping.java
│   │   │       ├── repository/
│   │   │       │   ├── DanhMucRepository.java
│   │   │       │   ├── SanPhamRepository.java
│   │   │       │   ├── SizeRepository.java
│   │   │       │   ├── TaiKhoanRepository.java
│   │   │       │   └── ToppingRepository.java
│   │   │       └── service/
│   │   │           ├── DanhMucService.java
│   │   │           ├── SanPhamService.java
│   │   │           ├── SizeService.java
│   │   │           ├── TaiKhoanService.java
│   │   │           └── ToppingService.java
│   │   └── resources/
│   │       ├── application.properties
│   │       ├── static/
│   │       │   ├── css/
│   │       │   │   ├── style.css
│   │       │   │   └── styleold.css
│   │       │   └── images/
│   │       │       ├── banner.png
│   │       │       ├── ca-phe-sua.jpg
│   │       │       ├── chocolate-macchiato.jpg
│   │       │       ├── matcha-macchiato.jpg
│   │       │       ├── tra-chanh-day.jpg
│   │       │       ├── tra-dao-cam-sa.jpg
│   │       │       ├── tra-dao-macchiato.jpg
│   │       │       ├── tra-dao.jpg
│   │       │       ├── tra-dau-macchiato.jpg
│   │       │       ├── tra-dau.png
│   │       │       ├── tra-dua-luoi.jpg
│   │       │       ├── tra-sua-bac-ha.jpg
│   │       │       ├── tra-sua-caramel.png
│   │       │       ├── tra-sua-chocolate.png
│   │       │       ├── tra-sua-dau.png
│   │       │       ├── tra-sua-hanh-nhan.png
│   │       │       ├── tra-sua-khoai-mon.png
│   │       │       ├── tra-sua-macchiato.jpg
│   │       │       ├── tra-sua-matcha.png
│   │       │       ├── tra-sua-oreo.jpg
│   │       │       ├── tra-sua-tran-chau-duong-den.png
│   │       │       ├── tra-sua-truyen-thong.png
│   │       │       ├── tra-tac-mat-ong.jpg
│   │       │       ├── tra-vai-macchiato.jpg
│   │       │       ├── tra-vai.jpg
│   │       │       └── tra-xoai.jpg
│   │       └── templates/
│   │           ├── dang-ky.html
│   │           ├── dang-nhap.html
│   │           ├── home.html
│   │           └── product-detail.html
│   └── test/
│       └── java/com/example/demoaaaaa/
│           └── DemoaaaaaApplicationTests.java
└── README.md
```

### Trách nhiệm các nhóm file

- `controller/`: ánh xạ route và điều phối dữ liệu sang template.
- `service/`: gọi repository và xử lý nghiệp vụ đăng ký khách hàng.
- `repository/`: các interface Spring Data JPA kế thừa `JpaRepository`.
- `model/`: entity JPA tương ứng với dữ liệu danh mục, sản phẩm, kích cỡ, topping và khách hàng.
- `templates/`: các trang Thymeleaf cho trang chủ, chi tiết sản phẩm, đăng ký và đăng nhập.
- `static/css/`, `static/images/`: stylesheet và ảnh được phục vụ như tài nguyên tĩnh.
- `application.properties`: cấu hình ứng dụng, datasource MySQL, JPA và Thymeleaf.
- `src/test/`: kiểm thử nạp Spring application context.
- `pom.xml`, `mvnw`, `mvnw.cmd`, `.mvn/wrapper/`: cấu hình Maven và Maven Wrapper.

## 4. Phân tích các thành phần chính

### Controller

- `Homecontroller`
  - `GET /`: lấy danh sách danh mục, sản phẩm và topping, sau đó render `home`.
  - `GET /san-pham/{id}`: tải sản phẩm theo ID cùng danh sách kích cỡ/topping để render `product-detail`; không tìm thấy sản phẩm thì ném HTTP 404.
- `TaiKhoanController`
  - `GET /dang-ky`: tạo đối tượng form `KhachHang` rỗng và render `dang-ky`.
  - `GET /dang-nhap`: render `dang-nhap` với đối tượng `KhachHang` trong model.
  - `POST /dang-ky`: nhận form `KhachHang`, kiểm tra validation và đối chiếu mật khẩu xác nhận; nếu hợp lệ thì gọi service, thông báo thành công qua flash attribute và chuyển hướng đến `/dang-nhap`. Nếu dữ liệu không hợp lệ hoặc service ném `IllegalArgumentException`, form đăng ký được render lại.

Các controller trả tên template/redirect; đây không phải controller REST trả JSON.

### Service

- `DanhMucService`: lấy toàn bộ danh mục qua `DanhMucRepository`.
- `SanPhamService`: lấy toàn bộ sản phẩm hoặc tìm sản phẩm theo ID; thao tác tìm ID trả về `Optional<SanPham>`.
- `SizeService`: lấy toàn bộ kích cỡ.
- `ToppingService`: lấy toàn bộ topping.
- `TaiKhoanService`: gán mã khách hàng dạng `KH` cộng thời gian hiện tại, luôn đặt ngày sinh thành `2002-01-01`, đặt giới tính thành `KHAC` nếu bị bỏ trống, rồi lưu qua `TaiKhoanRepository`.

### Repository

`DanhMucRepository`, `SanPhamRepository`, `SizeRepository`, `TaiKhoanRepository` và `ToppingRepository` đều kế thừa `JpaRepository` tương ứng. Code hiện chỉ gọi các thao tác dựng sẵn như `findAll`, `findById` và `save`; không khai báo truy vấn SQL hoặc phương thức truy vấn tùy chỉnh.

### Entity và luồng xử lý

Các entity được mô tả chi tiết tại [Cấu trúc dữ liệu và database](#6-cấu-trúc-dữ-liệu-và-database). Quan hệ được khai báo rõ trong code là `SanPham` có `@ManyToOne` tới `DanhMuc`.

Luồng trang chủ/chi tiết:

```text
Trình duyệt → Homecontroller → Service → JpaRepository → MySQL
                                      ↓
                               Thymeleaf template → HTML
```

Luồng đăng ký:

```text
Form HTML → TaiKhoanController → validation/kiểm tra xác nhận
                              → TaiKhoanService → TaiKhoanRepository → MySQL
                              → redirect /dang-nhap khi thành công
```

## 5. Danh sách API và route

Các đường dẫn sau là route giao diện web. Request thành công thường nhận HTML hoặc redirect, không phải JSON.

| Method | Endpoint | Chức năng | Tham số/Request body | Response |
|---|---|---|---|---|
| `GET` | `/` | Hiển thị danh mục, sản phẩm và dữ liệu topping. | Không có. | Render template `home` với model attributes `danhMucs`, `sanPhams`, `toppings`. |
| `GET` | `/san-pham/{id}` | Hiển thị chi tiết một sản phẩm cùng các kích cỡ và topping. | Path variable `id` kiểu `Integer`. | Render `product-detail` với `sanPham`, `toppings`, `sizes`; sản phẩm không tồn tại trả HTTP 404. |
| `GET` | `/dang-ky` | Hiển thị form tạo tài khoản khách hàng. | Không có. | Render `dang-ky`, model attribute `khachhang` là form mới. |
| `POST` | `/dang-ky` | Validate và đăng ký khách hàng. | Form URL-encoded, các trường bind vào `KhachHang`: `hoTen`, `soDienThoai`, `email`, `matKhau`, `confirmPassword`; `ngaySinh`, `gioiTinh`, `diaChi` là tùy chọn. Tên trường là tên thuộc tính Java được form Thymeleaf bind. | Thành công: redirect `302` tới `/dang-nhap`, kèm flash attribute `thongBao`. Không hợp lệ: render lại `dang-ky`; lỗi nghiệp vụ `IllegalArgumentException`: thêm `loiDangKy` vào model và render lại. |
| `GET` | `/dang-nhap` | Hiển thị trang đăng nhập. | Không có. | Render `dang-nhap`; hiện chưa có xử lý đăng nhập. |

### Ràng buộc form đăng ký trong code

- Họ tên, số điện thoại, email và mật khẩu không được để trống.
- Số điện thoại phải khớp biểu thức `^\\+?0\\d{9,12}$`.
- Email phải có định dạng email.
- Mật khẩu phải có ít nhất 8 ký tự.
- Mật khẩu xác nhận phải được nhập và giống mật khẩu.

## 6. Cấu trúc dữ liệu và database

Ứng dụng cấu hình MySQL với database `tra_sua_user`, địa chỉ mặc định `localhost:3306`. Driver là `com.mysql.cj.jdbc.Driver`. Username/password kết nối cần được thiết lập phù hợp với môi trường; không đưa thông tin xác thực vào README.

### Entity

| Entity | Bảng | Thuộc tính chính |
|---|---|---|
| `DanhMuc` | `danh_muc` | `idDanhMuc` (PK, identity), `tenDanhMuc`, `moTa`, `hinhAnh`. |
| `SanPham` | `san_pham` | `idSanPham` (PK, identity), `maSanPham`, `tenSanPham`, `moTa`, `hinhAnh`, `giaCoBan`, `trangThai`, `noiBat`, `banChay`, `danhMuc`. `danhMuc` là quan hệ `ManyToOne`, khóa ngoại cột `danh_muc_id`. |
| `Size` | `kich_thuoc` | `idSize` (PK, identity), `maSize`, `tenSize`, `phuThu`, `thuTu`, `trangThai`. |
| `Topping` | `topping` | `idTopping` (PK, identity), `tenTopping`, `maTopping`, `motaTopping`, `hinhAnh`, `giaBan`, `noiBat`, `trangThai`. |
| `KhachHang` | `khach_hang` | `khach_hang_id` (PK, identity), `ma_khach_hang`, `ho_ten`, `so_dien_thoai`, `email`, `mat_khau`, `ngay_sinh`, `gioi_tinh`, `dia_chi`, `diem_tich_luy`, `trang_thai`. `confirmPassword` là `@Transient`, không lưu thành cột. Mã khách hàng và email được đánh dấu unique. |

Ngoài quan hệ sản phẩm–danh mục nói trên, không thấy khai báo quan hệ JPA giữa các entity còn lại. Code cũng không cung cấp DDL, SQL seed dữ liệu hay migration; do đó không thể xác nhận đầy đủ schema vật lý hoặc nội dung dữ liệu ban đầu chỉ từ workspace.

### Tạo database và cấu hình kết nối

Tạo database rỗng trong MySQL nếu chưa có:

```sql
CREATE DATABASE IF NOT EXISTS tra_sua_user
  CHARACTER SET utf8mb4
  COLLATE utf8mb4_unicode_ci;
```

Ứng dụng đang đặt `spring.jpa.hibernate.ddl-auto=none`, vì vậy **không tự tạo bảng**. Cần nạp schema phù hợp với các entity trước khi chạy; dự án hiện không kèm script để thực hiện bước đó.

Cấu hình mặc định nằm trong `src/main/resources/application.properties`. Có thể cung cấp giá trị môi trường thay thế trước khi chạy (Spring Boot hỗ trợ biến môi trường tương ứng):

```powershell
$env:SPRING_DATASOURCE_URL = "jdbc:mysql://localhost:3306/tra_sua_user?useSSL=false&serverTimezone=Asia/Ho_Chi_Minh&allowPublicKeyRetrieval=true"
$env:SPRING_DATASOURCE_USERNAME = "ten_nguoi_dung_mysql"
$env:SPRING_DATASOURCE_PASSWORD = "mat_khau_mysql"
.\mvnw.cmd spring-boot:run
```

Không commit thông tin xác thực thật vào source control. `spring.jpa.show-sql=true` khiến câu lệnh SQL được ghi ra log.

## 7. Hướng dẫn cài đặt và chạy dự án

### Yêu cầu

- JDK 21.
- MySQL đang chạy và có thể truy cập từ máy chạy ứng dụng.
- Không bắt buộc cài Maven riêng nếu dùng Maven Wrapper; wrapper tải Maven 3.9.16 theo cấu hình dự án.

### Các bước

1. Clone repository hoặc mở thư mục source code hiện tại.
2. Tạo database `tra_sua_user` và nạp schema phù hợp. Lưu ý repository không có file SQL/migration, còn `ddl-auto=none` không tự tạo bảng.
3. Nếu cần, cấu hình các biến `SPRING_DATASOURCE_URL`, `SPRING_DATASOURCE_USERNAME` và `SPRING_DATASOURCE_PASSWORD` như ví dụ ở phần database.
4. Mở terminal tại thư mục chứa `pom.xml`, build dự án:

   ```powershell
   .\mvnw.cmd clean package
   ```

   Trên Linux/macOS:

   ```bash
   ./mvnw clean package
   ```

5. Khởi chạy ứng dụng:

   ```powershell
   .\mvnw.cmd spring-boot:run
   ```

   Trên Linux/macOS:

   ```bash
   ./mvnw spring-boot:run
   ```

6. Mở trình duyệt tại [http://localhost:8080/](http://localhost:8080/). Không có `server.port` được khai báo trong cấu hình nên ứng dụng dùng cổng mặc định Spring Boot là `8080`.
7. Các route giao diện đăng ký và đăng nhập là `/dang-ky` và `/dang-nhap`.

Để xác nhận ứng dụng sẵn sàng, xem log khởi động không có lỗi kết nối database rồi truy cập trang chủ. Trang cần dữ liệu bảng danh mục/sản phẩm để hiển thị nội dung; nếu schema hoặc dữ liệu chưa có, cần cấu hình database trước.

## 8. Ví dụ sử dụng

### Mở trang

```bash
curl -i http://localhost:8080/
curl -i http://localhost:8080/san-pham/1
curl -i http://localhost:8080/dang-ky
```

Thay `1` bằng ID sản phẩm có trong database. Trang chi tiết trả 404 nếu ID không tồn tại.

### Gửi form đăng ký

Ví dụ gửi các trường form mà controller bind vào `KhachHang`. Thay email và số điện thoại bằng dữ liệu chưa được dùng trong database vì email và mã khách hàng có ràng buộc unique.

```bash
curl -i -X POST http://localhost:8080/dang-ky \
  --data-urlencode "hoTen=Nguyen Van A" \
  --data-urlencode "soDienThoai=0912345678" \
  --data-urlencode "email=nguyen.a@example.com" \
  --data-urlencode "matKhau=MatKhau123" \
  --data-urlencode "confirmPassword=MatKhau123" \
  --data-urlencode "gioiTinh=NAM"
```

Nếu dữ liệu hợp lệ và lưu thành công, controller chuyển hướng tới `/dang-nhap`. Form trên chỉ minh họa request; thao tác lưu thực tế cần MySQL có bảng `khach_hang` tương thích.

## 9. Kiểm thử

Dự án có một test: `DemoaaaaaApplicationTests.contextLoads`, kiểm tra Spring application context có thể khởi động. Chạy bằng:

```powershell
.\mvnw.cmd test
```

Hoặc trên Linux/macOS:

```bash
./mvnw test
```

Đây chưa phải bộ kiểm thử nghiệp vụ cho các route. Vì context khởi động cùng cấu hình datasource, test có thể cần MySQL và schema hoạt động tùy cấu hình môi trường hiện tại.

## 10. Ghi chú và hướng phát triển

### Lưu ý từ trạng thái hiện tại

- Đăng nhập mới có giao diện; chưa có endpoint xử lý đăng nhập hoặc cơ chế xác thực.
- Kích cỡ/topping được tải để hiển thị trên trang chi tiết; chưa thấy xử lý gửi lựa chọn, tính tổng giá, giỏ hàng hay đặt hàng.
- Trong `TaiKhoanService`, đoạn mã hóa mật khẩu đang được comment và mật khẩu hiện được chuyển thẳng vào repository để lưu. Không nên dùng luồng đăng ký này cho môi trường thật trước khi tích hợp cơ chế băm mật khẩu an toàn.
- Controller chỉ bắt `IllegalArgumentException` từ đăng ký. Cách xử lý lỗi cơ sở dữ liệu/ràng buộc unique chưa được triển khai trong code hiện tại.
- Cần tự chuẩn bị schema và dữ liệu danh mục/sản phẩm/kích cỡ/topping vì repository không kèm SQL khởi tạo và Hibernate không tự tạo bảng.

### Đề xuất phát triển

- Bổ sung schema/migration và dữ liệu mẫu có thể tái tạo cho môi trường phát triển.
- Băm mật khẩu trước khi lưu và triển khai đầy đủ luồng đăng nhập.
- Thêm kiểm thử controller/service/repository cho đăng ký, dữ liệu sai và sản phẩm không tồn tại.
- Nếu nghiệp vụ yêu cầu, phát triển giỏ hàng, xử lý lựa chọn size/topping và tính giá; hiện các chức năng này chưa có trong code.
