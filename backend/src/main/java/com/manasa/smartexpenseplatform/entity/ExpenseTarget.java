package com.manasa.smartexpenseplatform.entity;
import java.util.List;
import jakarta.persistence.*;
import lombok.*;
@Entity
@Table(name = "expense_targets")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExpenseTarget {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

     @Column(name = "name", nullable = false, length = 100)
    private String name;

    @OneToMany(mappedBy = "expenseTarget")
    private List<Expense> expenses;
}
